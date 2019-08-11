<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="panel panel-default">
    <div class="panel-body">
        <div class="card" style="flex: 1; align-self:flex-start; margin-top: 20px">
            <div class="card-header"><h2>{{ topic }}</h2></div>
            <div class="card-body">
                <div style="display: flex; margin-top: 20px">

                    <div class="card" style="flex: 1; margin-right: 10px">
                        <div class="card-header"><h5>Info</h5></div>
                        <div class="card-body">
                            <p>When: {{ eventDateTime }}</p>
                            <p>Where: {{ eventAddress }}</p>
                        </div>
                    </div>

                    <div class="card" style="flex: 1; margin-left: 10px; margin-right: 10px">
                        <div class="card-header"><h5>Reports</h5></div>
                        <div class="card-body">
                            <ul>
                                <li ng-repeat="report in reports"><a ng-href="/reports/{{report.id}}">{{report.topic}}</a></li>
                                <a ng-href="#!{{id}}/addReport"> <button id="proposeReport" type="submit" class="btn btn-success" style="margin-top:30px">Propose report</button></a>
                            </ul>
                        </div>
                    </div>

                    <div class="card" style="flex: 1; margin-left: 10px">
                        <div class="card-header"><h5>Description</h5></div>
                        <div class="card-body">
                            <span> {{ description }} </span>
                        </div>
                    </div>
                </div>

                <form method="get">
                    <button id="registerToConference" type="submit" class="btn btn-success" style="margin-top:20px">{{ registrationAction }}</button>
                </form>

            </div>
            <div class="card-footer">
                <div class="card">
                    <div class="card-header"><h5>Registered guests</h5></div>
                    <div class="card-body">
                        <ol>
                            <li ng-repeat="user in registeredGuests">{{user.login}}</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>