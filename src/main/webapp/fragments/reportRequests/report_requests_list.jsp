<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="panel panel-default">
    <div class="panel-heading"><fmt:message key="label.report.requests"/></div>
    <div class="panel-body">
        <table class="table table-striped">
            <thead>
            <tr>
                <th><fmt:message key="column.id"/></th>
                <th><fmt:message key="label.topic"/></th>
                <th><fmt:message key="label.speaker"/></th>
                <th><fmt:message key="label.conference"/></th>
                <th><fmt:message key="label.actions"/></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="item in report_requests | orderBy: 'id'">
                <td>{{item.id}}</td>
                <td>{{item.topic}}</td>
                <td>{{item.speaker.surname}} {{item.speaker.name}}</td>
                <td>{{item.conference.topic}}</td>
                <td>
                    <button class="btn btn-success" style="margin-top:5px" ng-click="processRequest(item.id, true)">
                        <fmt:message key="button.approve"/>
                    </button>

                    <button class="btn btn-danger" style="margin-top:5px" ng-click="processRequest(item.id, false)">
                        <fmt:message key="button.reject"/>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>