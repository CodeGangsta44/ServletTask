<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="title.registration"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.2/angular.min.js"></script>
    <script src="http://code.jquery.com/jquery-1.8.3.js"></script>

</head>
<body ng-app="home" ng-controller="AppCtrl">
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="container" style="margin-top: 60px">
    <div class="row justify-content-center">
        <div class="col-md-8 col-md-offset-2">
            <div>
                <label id="surnameLabel"><fmt:message key="label.surname"/>: {{surname}}</label>
            </div>

            <div>
                <label id="nameLabel"><fmt:message key="label.name"/>: {{name}}</label>
            </div>

            <div>
                <label id="patronymicLabel"><fmt:message key="label.patronymic"/>: {{patronymic}}</label>
            </div>

            <div>
                <label id="loginLabel"><fmt:message key="label.login"/>: {{login}}</label>
            </div>

            <div>
                <label id="emailLabel"><fmt:message key="label.email"/>: {{email}}</label>
            </div>

            <div>
                <label id="rolesLabel"><fmt:message key="label.roles"/>:</label>
                <ul>
                    <li ng-repeat="role in roles">{{role}}</li>
                </ul>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript" src="/js/home.js"></script>
</body>
</html>