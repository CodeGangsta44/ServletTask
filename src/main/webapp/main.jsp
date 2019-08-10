<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="title.registration"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.7.8/angular.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</head>
<body ng-app="main" ng-controller="AppCtrl">
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="container-fluid" style="margin-top: 30px; width: 100%">
    <div class="row justify-content-center">
        <div class="col-md-12">
            <%@include file="fragments/header.jsp" %>
            <div style="display: flex; margin-top: 20px;">
                <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel" style="flex: 4; margin-right: 10px">
                    <ol class="carousel-indicators">
                        <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
                        <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
                        <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
                    </ol>
                    <div class="carousel-inner">
                        <div class="carousel-item active">
                            <img src="/img/conferences.jpg" class="d-block w-100" alt="..."
                                 style="object-fit: cover; height: 700px; width: 1000px">
                            <div class="carousel-caption d-none d-md-block" style="background: rgba(0, 0, 0, 0.5); border-radius: 25px">
                                <h4><fmt:message key="greeting.conferences"/></h4>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <img src="/img/scope.jpeg" class="d-block w-100" alt="..."
                                 style="object-fit: cover; height: 700px; width: 1000px">
                            <div class="carousel-caption d-none d-md-block" style="background: rgba(0, 0, 0, 0.5); border-radius: 25px">
                                <h4><fmt:message key="greeting.scope"/></h4>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <img src="/img/speaker.jpg" class="d-block w-100" alt="..."
                                 style="object-fit: cover; height: 700px; width: 1000px">
                            <div class="carousel-caption d-none d-md-block" style="background: rgba(0, 0, 0, 0.5); border-radius: 25px">
                                <h4><fmt:message key="greeting.speaker"/></h4>
                            </div>
                        </div>
                    </div>
                    <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>

                <div style="flex: 1; margin-left: 10px; position: relative">
                    <img src="/img/logo/conf.hub.png" class="d-block w-100" alt="..." style="position: absolute; margin-bottom: 20px">
                    <div class="card" style="position: absolute; bottom: 0">
                        <div class="card-header">
                            <p><h5><fmt:message key="greeting.message"/></h5></p>
                        </div>
                        <div class="card-body">
                            <form action="/app/login">
                                <button type="submit" class="btn btn-success" style="margin-top: 5px; width: 100%"><fmt:message key="button.login"/></button>
                            </form>
                            <form action="/app/registration">
                                <button type="submit" class="btn btn-success" style="margin-top: 5px; width: 100%"><fmt:message key="button.registration"/></button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <%@include file="fragments/footer.jsp" %>
        </div>
    </div>
</div>
<script type="text/javascript" src="/js/main.js"></script>
</body>
</html>