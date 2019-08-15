<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="panel panel-default">
    <div class="panel-heading">Create conference</div>
    <div class="panel-body">
        <form style="margin-bottom: 30px" name="form_name" autocomplete="off" novalidate ng-submit="sendForm(form)">
            <div class="form-group">
                <label id="inputTopicLabel" for="topicInput">Topic</label>
                <input type="text"
                       class="form-control"
                       id="topicInput"
                       placeholder="Topic"
                       required
                       ng-model="form.topic">
            </div>
            <div class="form-group">
                <label id="inputNameLabel" for="dateTimeInput">Date and time of event</label>
                <input type="text"
                       class="form-control"
                       id="dateTimeInput"
                       placeholder="When"
                       required
                       ng-model="form.eventDateTime">
            </div>
            <div class="form-group">
                <label id="inputPatronymicLabel" for="addressInput">Address of event</label>
                <input type="text"
                       class="form-control"
                       id="addressInput"
                       placeholder="Where"
                       required
                       ng-model="form.eventAddress">
            </div>
            <div class="form-group">
                <label id="inputLoginLabel" for="descriptionInput">Description</label>
                <input type="text"
                       class="form-control"
                       id="descriptionInput"
                       placeholder="About this event"
                       required
                       ng-model="form.description">
            </div>
            <button type="submit" class="btn btn-success" style="margin-top:30px" ng-disabled="form.$invalid">
                Create conference
            </button>
        </form>
    </div>
</div>