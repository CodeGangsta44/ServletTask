let app = angular.module("conferences", ['ngRoute']);


function showData(data, $scope) {
    console.log(data);
    $scope.action = 'app/api/conferences/' + data.data.id + '/changeRegistration';
    Object.assign($scope, data.data);

    $scope.registrationAction = $scope.registered ? 'Cancel registration' : 'Register';
    console.log($scope.registrationAction);
}

function getConferencesList($scope, $http) {
    $http.get("app/api/conferences" + $scope.path)
        .then(
            (data)=>{
                console.log('SUCCESS');
                console.log(data);
                $scope.conferences = data.data;
            },
            (error) => {
                console.log('FAIL');
                console.log(data);
                console.log(error.data);
                resultMessageEl.style.color = 'red';
                $scope.message = error.data.message;
            })
}

function getConferencesPage($scope, $http) {
    $http.get("/app/api/conferences", { params : { page : $scope.currentPage, capacity : $scope.currentCapacity } })
        .then(
            (data) => {
                console.log(data);
                $scope.conferences = data.data;
                $scope.highLightButtons();
            },
            (error) => {
                console.log(error);
            }
        )
}

function getTotalConferencesNumber($scope, $http) {
    $http.get("/app/api/conferences/totalNumber")
        .then(
            (data) => {
                $scope.totalNumber = data.data;
                console.log(data);
            },
            (error) => {
                console.log(error);
            }
        )
}


app.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'app/fragments/conference/conferences_list',
            controller: 'ConferencesListCtrl'
        })
        .when('/requests', {
            templateUrl: 'app/fragments/conference/conference_requests_list',
            controller: 'ConferenceRequestsListCtrl'
        })
        .when('/finished', {
            templateUrl: 'app/fragments/conference/finished_conferences_list',
            controller: 'FinishedConferencesListCtrl'
        })
        .when('/notFinished', {
            templateUrl: 'app/fragments/conference/not_finished_conferences_list',
            controller: 'NotFinishedConferencesListCtrl'
        })
        .when('/me', {
            templateUrl: 'app/fragments/conference/conferences_list',
            controller: 'MyConferencesListCtrl'
        })
        .when('/add', {
            templateUrl: 'app/fragments/conference/conference_form',
            controller: 'CreateConferenceCtrl'
        })
        .when('/:id', {
            templateUrl: 'app/fragments/conference/conference_info',
            controller: 'ConferenceCtrl'
        })
        .when('/:id/addReport', {
            templateUrl: 'app/fragments/report/report_form',
            controller: 'CreateReportCtrl'
        })
});

function answerRequest($scope, $http, id, answer) {
    console.log(answer);
    $http({
        method: "POST",
        url: "app/api/conferences/" + id + "/processRequest",
        data: answer,
        headers: { "Content-Type" : "application/json" }
    }).then(
        (data) => {
            console.log(data);
            getConferencesList($scope, $http)
        },
        (error) => {
            console.log(error);
        }
    )
}

app.controller("ConferencesListCtrl", function ($scope, $http) {
    $scope.conferences = [];
    $scope.path = '/';
    $scope.currentPage = 1;
    $scope.numberOfPages = 0;
    $scope.currentCapacity = 'All';
    getConferencesList($scope, $http);
    getConferencesPage($scope, $http);
    getTotalConferencesNumber($scope, $http);

    $scope.selectPage = (number) => {
        console.log(number);
        $scope.currentPage = number;
        getConferencesPage($scope, $http);
    };

    $scope.updateButtons = () => {
        if ($scope.currentCapacity === 'All') {
            getConferencesList($scope, $http);
            document.getElementById('topPageButtonGroup').hidden = true;
            document.getElementById('bottomPageButtonGroup').hidden = true;
        } else {
            document.getElementById('topPageButtonGroup').hidden = false;
            document.getElementById('bottomPageButtonGroup').hidden = false;
            getTotalConferencesNumber($scope, $http);
            $scope.numberOfPages = Math.ceil($scope.totalNumber / $scope.currentCapacity);
            getConferencesPage($scope, $http);
        }
    };

    $scope.highLightButtons = () => {
        for (let i = 1; i <= $scope.numberOfPages; i++) {
            document.getElementById('pageButton-top-' + i).classList.remove('active');
            document.getElementById('pageButton-bottom-' + i).classList.remove('active');
        }
        document.getElementById('pageButton-top-' + $scope.currentPage).classList.add('active');
        document.getElementById('pageButton-bottom-' + $scope.currentPage).classList.add('active');
    };

    $scope.checkButtonsConditions = () => {
        if ($scope.currentPage === 1) {
            let elements = document.getElementsByClassName('previous-page-button');
            Array.prototype.forEach.call(elements, function(el) {
                el.classList.add('disabled');
            });
        }

        if ($scope.currentPage > 1) {
            let elements = document.getElementsByClassName('previous-page-button');
            Array.prototype.forEach.call(elements, function(el) {
                el.classList.remove('disabled');
            });
        }

        if ($scope.currentPage === $scope.numberOfPages) {
            let elements = document.getElementsByClassName('next-page-button');
            Array.prototype.forEach.call(elements, function(el) {
                el.classList.add('disabled');
            });
        }

        if ($scope.currentPage < $scope.numberOfPages) {
            let elements = document.getElementsByClassName('next-page-button');
            Array.prototype.forEach.call(elements, function(el) {
                el.classList.remove('disabled');
            });
        }
    };

    $scope.previousPage = () => {
        if ($scope.currentPage > 1) {
            $scope.currentPage --;
            $scope.checkButtonsConditions();
            getConferencesPage($scope, $http);
        }
    };

    $scope.nextPage = () => {
        if ($scope.currentPage < $scope.numberOfPages) {
            $scope.currentPage ++;
            $scope.checkButtonsConditions();
            getConferencesPage($scope, $http);
        }
    }
});

app.controller("ConferenceRequestsListCtrl", function ($scope, $http) {
    $scope.conferences = [];
    $scope.path = '/requests';

    getConferencesList($scope, $http);

    $scope.processRequest = (id, answer) => answerRequest.call(this, $scope, $http, id, answer);
});

app.controller("NotFinishedConferencesListCtrl", function ($scope, $http) {
    $scope.conferences = [];
    $scope.path = '/';
    getConferencesList($scope, $http);

    $scope.prepareForConferenceFinishing = (id) => {
        $scope.conferenceIdToFinish = id;
    };

    $scope.cancelConferenceFinishing = () => {
        delete $scope.conferenceIdToFinish;
    };

    $scope.finishConference = () => {
        console.log($scope.numberOfVisitedGuests);
        $http({
            method: "PUT",
            url: "app/api/conferences/" + $scope.conferenceIdToFinish + "/finish",
            data: $scope.numberOfVisitedGuests,
            headers: {"Content-Type" : "application/json"}
        }).then(
            (data) => {
                console.log(data);
                getConferencesList($scope, $http);
            },
            (error) => console.log(error)
        )
    }
});

app.controller("FinishedConferencesListCtrl", function ($scope, $http) {
    $scope.conferences = [];
    $scope.path = '/finished';
    getConferencesList($scope, $http);
});

app.controller("MyConferencesListCtrl", function ($scope, $http) {
    $scope.conferences = [];
    $scope.path = '/me';
    getConferencesList($scope, $http);
});

app.controller("ConferenceCtrl", function ($scope, $http, $routeParams) {
    $scope.reports = [];
    $scope.registeredGuests = [];

    let regButton = document.getElementById('registerToConference');

    $http.get('app/api/conferences/' + $routeParams.id)
        .then (
            (data) => showData(data, $scope),
            (error) => {
                console.log(error);
            }
        );

    regButton.onclick = () => {
        $http.get($scope.action)
            .then (
                (data) => showData(data, $scope),
                (error) => console.log(error)
            );

    }
});

app.controller("CreateConferenceCtrl", function ($scope, $http) {
    $scope.form = {};
    
    $scope.sendForm = function(form){
        console.log(form);
        $http({
            method: "POST",
            url: "app/api/conferences",
            data: JSON.stringify(form),
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

app.controller("CreateReportCtrl", function ($scope, $http, $routeParams) {
    $scope.form = {};

    $scope.sendForm = function(form){
        console.log(form);
        $http({
            method: "POST",
            url: "app/api/reportRequests/request/" + $routeParams.id,
            data: JSON.stringify(form),
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

app.controller("ReportCtrl", function ($scope, $http, $routeParams) {

    $http.get('app/api/reports/' + $routeParams.id)
        .then (
            (data) => showData(data, $scope),
            (error) => {
                console.log(error);
            }
        );
});