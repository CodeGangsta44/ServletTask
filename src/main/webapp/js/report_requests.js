let app = angular.module("report_requests", ['ngRoute']);


app.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'app/fragments/reportRequests/report_requests_list',
            controller: 'ReportRequestsListCtrl'
        })
        .when('/me', {
            templateUrl: 'app/fragments/reportRequests/report_proposes_list',
            controller: 'ReportProposesListCtrl'
        })
});

function getListOfRequests($scope, $http) {
    $http.get("app/api/reportRequests" + $scope.path)
        .then(
            (data)=>{
                console.log(data);
                $scope.report_requests = data.data;
            },
            (error) => {
                console.log(error.data);
                $scope.message = error.data.message;
            })
}

function answerRequest($http , $scope, id, answer) {
    console.log(answer);
    $http({
        method: "POST",
        url: "app/api/reportRequests/" + id,
        data: answer,
        headers: { "Content-Type" : "application/json" }
    }).then(
        (data) => {
            console.log(data);
            console.dir($scope.report_requests);

            getListOfRequests($scope, $http)
        },
        (error) => {
            console.log(error);
        }
    )
}

app.controller("ReportRequestsListCtrl", function ($scope, $http) {
    console.log("IN CONTROLLER");
    $scope.report_requests = [];
    $scope.path = '/';

    $scope.processRequest = (id, answer) => answerRequest.call(this, $http, $scope, id, answer);

    getListOfRequests($scope, $http)

});

app.controller("ReportProposesListCtrl", function ($scope, $http) {
    console.log("IN CONTROLLER");
    $scope.report_requests = [];
    $scope.path = '/me';

    $scope.processRequest = (id, answer) => answerRequest.call(this, $http, $scope, id, answer);

    getListOfRequests($scope, $http, '/me')

});