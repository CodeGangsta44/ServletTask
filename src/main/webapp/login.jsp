<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="form.login.name"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>

<div class="container" style="margin-top: 60px">
    <div class="row justify-content-center">
        <div class="col-md-8 col-md-offset-2">
            <h2 class="page-header"><fmt:message key="form.login.name"/></h2>

            <c:if test="${param.logout ne null}" >
                <div class="alert alert-success" role="alert"><fmt:message key="logout"/></div>
            </c:if>

            <c:if test="${param.error ne null}" >
                <div class="alert alert-warning" role="alert"><fmt:message key="login.error"/></div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/app/api/users/authenticate" style="margin-bottom: 30px" name="form" autocomplete="off" >
                <div class="form-group">
                    <label id="inputLoginLabel" for="exampleInputLogin"><fmt:message key="label.login"/></label>
                    <input type="text"
                           name="login"
                           class="form-control"
                           id="exampleInputLogin"
                           placeholder="<fmt:message key="label.login"/>">
                </div>
                <div class="form-group">
                    <label id="inputPasswordLabel" for="exampleInputPassword"><fmt:message key="label.password"/></label>
                    <input type="text"
                           name="password"
                           class="form-control"
                           id="exampleInputPassword"
                           placeholder="<fmt:message key="label.password"/>">
                </div>
                <button type="submit" class="btn btn-success" style="margin-top:30px" >
                    <fmt:message key="button.login"/>
                </button>
            </form>
            <div class="alert alert-info" role="alert">
                <a href="/app/registration"><fmt:message key="title.registration"/></a>

                <span style="float: right">
                        <a href="?lang=en"><fmt:message key="language.en"/></a>
                        |
                        <a href="?lang=ua"><fmt:message key="language.ua"/></a>
                </span>
            </div>

        </div>
    </div>
</div>
</body>
</html>