angular.module("home", [])
    .controller("AppCtrl", function ($scope, $http) {

        $http.get("/api/users/me")
            .then(
                (data)=>{
                    console.log(data);
                    Object.assign($scope, data.data);
                },
                (error) => {
                    console.log(error.data);
                })
    });