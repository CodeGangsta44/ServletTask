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
<body ng-app="registration_form" ng-controller="AppCtrl">
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="container" style="margin-top: 60px">
    <div class="row justify-content-center">
        <div class="col-md-8 col-md-offset-2">

            <div class="alert alert-success" role="alert" id="resultMessage" style="visibility: hidden"></div>
            <h2 class="page-header"><fmt:message key="form.registration.name"/></h2>
            <form style="margin-bottom: 30px" name="form" autocomplete="off" novalidate ng-submit="sendForm(auth)">
                <div class="form-group">
                    <label id="inputSurnameLabel" for="exampleInputSurname"><fmt:message key="label.surname"/></label>
                    <input type="text"
                           class="form-control"
                           id="exampleInputSurname"
                           placeholder="<fmt:message key="label.surname"/>"
                           required
                           ng-model="auth.surname">
                </div>
                <div class="form-group">
                    <label id="inputNameLabel" for="exampleInputName"><fmt:message key="label.name"/></label>
                    <input type="text"
                           class="form-control"
                           id="exampleInputName"
                           placeholder="<fmt:message key="label.name"/>"
                           required
                           ng-model="auth.name">
                </div>
                <div class="form-group">
                    <label id="inputPatronymicLabel" for="exampleInputPatronymic"><fmt:message key="label.patronymic"/></label>
                    <input type="text"
                           class="form-control"
                           id="exampleInputPatronymic"
                           placeholder="<fmt:message key="label.patronymic"/>"
                           required
                           ng-model="auth.patronymic">
                </div>
                <div class="form-group">
                    <label id="inputLoginLabel" for="exampleInputLogin"><fmt:message key="label.login"/></label>
                    <input type="text"
                           class="form-control"
                           id="exampleInputLogin"
                           placeholder="<fmt:message key="label.login"/>"
                           required
                           ng-model="auth.login">
                </div>
                <div class="form-group">
                    <label id="inputEmailLabel" for="exampleInputEmail"><fmt:message key="label.email"/></label>
                    <input type="text"
                           class="form-control"
                           id="exampleInputEmail"
                           placeholder="<fmt:message key="label.email"/>"
                           required
                           ng-model="auth.email">
                </div>
                <div class="form-group">
                    <label id="inputPasswordLabel" for="exampleInputPassword"><fmt:message key="label.password"/></label>
                    <input type="text"
                           class="form-control"
                           id="exampleInputPassword"
                           placeholder="<fmt:message key="label.password"/>"
                           required
                           ng-model="auth.password">
                </div>
                <button type="submit" class="btn btn-success" style="margin-top:30px" ng-disabled="form.$invalid">
                    <fmt:message key="button.registration"/>
                </button>
            </form>

            <span style="float: right">
                        <a href="?lang=en"><fmt:message key="language.en"/></a>
                        |
                        <a href="?lang=ua"><fmt:message key="language.ua"/></a>
            </span>

        </div>
    </div>
</div>
<script type="text/javascript" src="/js/reg_form.js"></script>
</body>
</html>