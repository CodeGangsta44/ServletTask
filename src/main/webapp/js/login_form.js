angular.module("login_form",[])
    .controller("AppCtrl", function ($scope, $http) {
        $scope.auth = {};

        let resultMessageEl = document.getElementById('resultMessage');
        let exampleInputLoginEl = document.getElementById('exampleInputLogin');
        let exampleInputPasswordEl = document.getElementById('exampleInputPassword');

        let inputLoginLabel = document.getElementById('inputLoginLabel');
        let inputPasswordLabel = document.getElementById('inputPasswordLabel');

        exampleInputLoginEl.addEventListener('input', () => {
            inputLoginLabel.style.color = 'black';
            inputPasswordLabel.style.color = 'black';
            $scope.message = '';
        });
        $scope.sendForm = function(auth){
            $http({
                method: "POST",
                url: "app/api/login",
                data: $.param(auth),
                headers: { "Content-Type" : "application/x-www-form-urlencoded" }
            }).then(
                (data) => {
                    resultMessageEl.style.color = 'green';
                    $scope.message = 'Logged in';
                    exampleInputLoginEl.value = '';
                    exampleInputPasswordEl.value = '';
                    inputLoginLabel.style.color = 'black';
                    console.log(data);
                },
                (error) => {
                    console.log(error.data);

                    resultMessageEl.style.color = 'red';
                    inputLoginLabel.style.color = 'red';
                    inputPasswordLabel.style.color = 'red';

                    exampleInputPasswordEl.value = '';
                    $scope.message = error.data.message;
                }
            );
        }
    })