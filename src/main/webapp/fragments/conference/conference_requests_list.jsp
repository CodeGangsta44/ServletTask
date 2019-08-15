<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="panel panel-default">
    <div class="panel-heading"><fmt:message key="label.conferences.requests"/></div>
    <div class="panel-body">
        <table class="table table-striped">
            <thead>
            <tr>
                <th><fmt:message key="column.id"/></th>
                <th><fmt:message key="label.topic"/></th>
                <th><fmt:message key="label.date.and.time"/></th>
                <th><fmt:message key="label.address"/></th>
                <th><fmt:message key="label.actions"/></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="item in conferences | orderBy: 'id'">
                <td>{{item.id}}</td>
                <td><a ng-href="#!{{item.id}}">{{item.topic}}</a></td>
                <td>{{item.eventDateTime}}</td>
                <td>{{item.eventAddress}}</td>

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