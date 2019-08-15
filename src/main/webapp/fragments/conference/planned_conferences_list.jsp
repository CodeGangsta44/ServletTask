<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="container" style="margin-top: 20px">
    <div class="panel-body">
        <table class="table table-striped">
            <thead>
            <tr>
                <th><fmt:message key="column.id"/></th>
                <th><fmt:message key="label.topic"/></th>
                <th><fmt:message key="label.date.and.time"/></th>
                <th><fmt:message key="label.address"/></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="item in conferences | orderBy: 'id'">
                <td>{{item.id}}</td>
                <td><a ng-href="#!{{item.id}}">{{item.topic}}</a></td>
                <td>{{item.eventDateTime}}</td>
                <td>{{item.eventAddress}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
