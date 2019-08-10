let app = angular.module("users", ['ngRoute']);

app.directive('file', function () {
    return {
        scope: {
            file: '='
        },
        link: function (scope, el, attrs) {
            el.bind('change', function (event) {
                var file = event.target.files[0];
                scope.file = file ? file : undefined;
                scope.$apply();
            });
        }
    };
});


app.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'app/fragments/users/user_list',
            controller: 'UserListCtrl'
        })
        .when('/me', {
            templateUrl: 'app/fragments/users/home',
            controller: 'HomeCtrl'
        })
        .when('/:id', {
            templateUrl: 'app/fragments/users/user',
            controller: 'UserCtrl'
        })
});

function getListOfUsers($scope, $http) {
    $http.get("app/api/users")
        .then(
            (data)=>{
                console.log(data);
                $scope.users = JSON.parse(data.data);
            },
            (error) => {
                console.log(error.data);
                resultMessageEl.style.color = 'red';
                $scope.message = error.data.message;
            });
}

function drawLines($scope) {
    let maxVotes = Math.max.apply(null, Object.values($scope.votes));


    $scope.votesMap = {
        5: 0,
        4: 0,
        3: 0,
        2: 0,
        1: 0
    };

    Object.assign($scope.votesMap, $scope.votes);

    console.log(Object.values($scope.votes));


    for (let key in $scope.votesMap) {
        let idOfLine = 'line-' + key;
        let idOfLineSpan = 'line-span-' + key;
        document.getElementById(idOfLine).style.width = (50 * $scope.votesMap[key] / maxVotes) + '%';
        document.getElementById(idOfLineSpan).textContent = $scope.votesMap[key];
    }


}

function fillStars($scope) {
    for (let i = 1; i <= 5; i++){
        let resultId = 'star-' + i;
        document.getElementById(resultId).style.color = 'lightgray';
    }

    console.log($scope.votes);
    let totalRating = 0;
    let totalMarks = 0;

    for (let key in $scope.votes) {
        totalRating += key * $scope.votes[key];
        totalMarks += $scope.votes[key];
    }

    $scope.averageRating = Math.round(totalRating / totalMarks * 100) / 100;

    for (let i = 1; i <= $scope.averageRating; i++){
        let resultId = 'star-' + i;
        document.getElementById(resultId).style.color = '#28a745';
    }
}

function setMarkOfCurrentUser($scope, $http) {
    $http.get("app/api/votes/" + $scope.user.id + "/me")
        .then(
            (data) => {
                console.log(data);
                $scope.currentUserMark = data.data;
            },
            (error) => {
                console.log(error);
                $scope.currentUserMark = 'You did not marked this user yet';
            }
        );
}

function getVotesOfSpeaker($scope, $http) {
    $http.get("app/api/votes/" + $scope.user.id)
        .then(
            (data) => {
                console.log(data);
                $scope.votes = JSON.parse(data.data);
                fillStars($scope);
                drawLines($scope);
                setMarkOfCurrentUser($scope, $http);
            },
            (error) => {
                console.log(error);
            }
        );
}

function getSpeakerStatistics($scope, $http) {
    $http.get("app/api/users/speakerStatistics/" + $scope.user.id)
        .then(
            (data) => {
                $scope.speakerStatistics = JSON.parse(data.data);
                console.log(data);
            },
            (error) => {
                console.log(error);
            }
        )
}

function getSpeakerInfo($scope, $http) {
    console.log('Init stars');
    $scope.votes = {};

    getVotesOfSpeaker($scope, $http);

    getSpeakerStatistics($scope, $http)
}

function getUserInfo($scope, $http) {
    $http.get("app/api/users" + $scope.path)
        .then(
            (data)=>{
                console.log(data);
                Object.assign($scope.user, JSON.parse(data.data));

                if ($scope.user.roles.indexOf('SPEAKER') !== -1) {
                    document.getElementById("additionalSpeakerInformation").hidden = false;
                    $scope.isSpeaker = true;
                    getSpeakerInfo($scope, $http);
                }
            },
            (error) => {
                console.log(error.data);
            });
}

function sendVote($scope, $http, mark) {
    $http({
        method: "POST",
        url: "app/api/votes/" + $scope.user.id,
        data: mark,
        headers: { "Content-Type" : "application/json" }
    }).then(
        (data) => {
            console.log(data);
            getVotesOfSpeaker($scope, $http);
        },
        (error) => {
            console.log(error);
        }
    )
}

app.controller("HomeCtrl", function ($scope, $http) {
    $scope.user = {};
    $scope.editForm = {};
    $scope.path = '/me';

    $scope.isSpeaker = false;

    getUserInfo($scope, $http);

    $scope.changeAvatar = () => {

        let formData = new FormData();

        formData.append('file', $scope.file);

        console.dir($scope.file);

        $http({
            method: "POST",
            url: "app/api/users/" + $scope.user.id + "/changeAvatar",
            data: formData,
            headers: { "Content-Type" : undefined },
        }).then(
            (data) => {
                console.log(data);
                getUserInfo($scope, $http);
            },
            (error) => {
                console.log(error);
            }
        )
    };

    $scope.editProfile = () => {
        let resultMessageEl = document.getElementById('resultMessage');
        $http({
            method: "PUT",
            url: "app/api/users",
            data: JSON.stringify($scope.user),
            headers: {"Content-Type" : "application/json"}
        }).then(
            (data) => {
                console.log(data);
                getUserInfo($scope, $http);

                resultMessageEl.className = 'alert alert-success';
                resultMessageEl.innerText = data.data;
                resultMessageEl.style.visibility='visible';
            },
            (error) => {
                console.log(error);

                resultMessageEl.className = 'alert alert-warning';
                resultMessageEl.innerText = error.data.localizedMessage;
                resultMessageEl.style.visibility='visible';
            }
        )
    }

    $scope.toggleSpeakerSelection = () => {

        if ($scope.isSpeaker) {
            $scope.isSpeaker = false;
            let index = $scope.user.roles.indexOf('SPEAKER');
            $scope.user.roles.splice(index, 1);

        } else {
            $scope.isSpeaker = true;
            $scope.user.roles.push('SPEAKER');
        }

    };
});

app.controller("UserListCtrl", function ($scope, $http) {
    $scope.users = [];
    $scope.editForm = {};
    $scope.roles = ['ADMIN', 'MODER', 'SPEAKER', 'USER'];

    getListOfUsers($scope, $http);

    $scope.prepareToDeleteUser = (id) => {
        console.log(id);
        $scope.userIdToDelete = id;
    };

    $scope.cancelUserDeleting = () => {
        delete $scope.userIdToDelete;
    };

    $scope.deleteUser = () => {
        console.log($scope.userIdToDelete);
        $http.delete("app/api/users/" + $scope.userIdToDelete)
            .then(
                (data) => {
                    console.log(data);
                    getListOfUsers($scope, $http);
                },
                (error) => {
                    console.log(error);
                }
            );
    };

    $scope.editUser = (user) => {
        Object.assign($scope.editForm, user);
        $scope.editForm.roles = [];
        Object.assign($scope.editForm.roles, user.roles);
    };

    $scope.toggleRoleSelection = (role) => {
      let index = $scope.editForm.roles.indexOf(role);

      if (index === -1) {
          $scope.editForm.roles.push(role);
      } else {
          $scope.editForm.roles.splice(index, 1);
      }

    };

    $scope.saveChanges = () => {
        let resultMessageEl = document.getElementById('resultMessage');
        $http({
            method: "PUT",
            url: "app/api/users",
            data: JSON.stringify($scope.editForm),
            headers: {"Content-Type" : "application/json"}
        }).then(
            (data) => {
                console.log(data);

                getListOfUsers($scope, $http);
                resultMessageEl.className = 'alert alert-success';
                resultMessageEl.innerText = JSON.parse(data.data);
                resultMessageEl.style.visibility='visible';
            },
            (error) => {
                console.log(error);
                resultMessageEl.className = 'alert alert-warning';
                resultMessageEl.innerText = error.data.localizedMessage;
                resultMessageEl.style.visibility='visible';
            }
        );
    };

    $scope.prepareToProposeReport = (user) => {
        $scope.conferences = [];
        $scope.reportProposeForm = {};

        $scope.reportProposeForm.speaker = user;

        $http.get("/api/conferences")
            .then(
                (data)=>{
                    console.log(data);
                    $scope.conferences = data.data;
                },
                (error) => {
                    console.log(error.data);
                })
    };

    $scope.proposeReport = () => {
        console.log($scope.reportProposeForm);
        console.log($scope.chosenConferenceId);
        $http({
            method: "POST",
            url: "app/api/reportRequests/request/" + $scope.chosenConferenceId,
            data: JSON.stringify($scope.reportProposeForm),
            headers: { "Content-Type" : "application/json" }
        }).then(
            (data) => {
                console.log(data);
            },
            (error) => {
                console.log(error);
            }
        );
    }

});

app.controller("UserCtrl", function ($scope, $http, $routeParams) {
    $scope.user = {};
    $scope.isSpeaker = false;
    $scope.path = '/' + $routeParams.id;

    getUserInfo($scope, $http);

    $scope.clickStar = (mark) => {
        sendVote($scope, $http, mark);
    };

    console.log($scope.isSpeaker);
});