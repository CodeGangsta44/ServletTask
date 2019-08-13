<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<link rel="stylesheet" type="text/css" href="../css/styles.css">
<nav class="navbar navbar-expand-lg navbar-light bg-light" id="header">
    <a class="navbar-brand" href="/app/">
        <img src="/img/logo/conf.hub.png" alt="..." width="50" height="50">
    </a>

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a class="nav-link user-option" href="/app/users#!/me"><h6><fmt:message key="label.home"/></h6><span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item user-option">
                <a class="nav-link" href="/app/notifications"><h6><fmt:message key="label.notifications"/></h6></a>
            </li>

            <li class="nav-item dropdown user-option">
                <a class="nav-link" href="" id="navbarDropdownUser" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <h6><fmt:message key="label.conferences"/></h6>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item user-option" href="/app/conferences"><fmt:message key="label.all.conferences"/></a>
                    <a class="dropdown-item user-option" href="/app/conferences#!/me"><fmt:message key="label.planned.conferences"/></a>
                </div>
            </li>

            <li class="nav-item dropdown speaker-option moder-option">
                <a class="nav-link" href="" id="navbarDropdownSpeaker" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <h6><fmt:message key="label.reports"/></h6>
                </a>
                <div class="dropdown-menu " aria-labelledby="navbarDropdown">
                    <a class="dropdown-item speaker-option" href="#"><fmt:message key="label.planned.reports"/></a>
                    <a class="dropdown-item speaker-option" href="/app/reportRequests#!/me"><fmt:message key="label.report.propositions"/></a>
                    <a class="dropdown-item moder-option" href="/app/reportRequests"><fmt:message key="label.report.requests"/></a>
                </div>
            </li>

            <li class="nav-item admin-option">
                <a class="nav-link" href="/app/users"><h6><fmt:message key="label.all.users"/></h6></a>
            </li>


        </ul>

        <span class="glyphicon glyphicon-log-out user-option" aria-hidden="true" style="margin-left: 5px; float: right">
            <a href="/app/logout"><fmt:message key="label.logout"/></a>
        </span>
    </div>
</nav>
<script type="text/javascript" src="/js/header.js"></script>