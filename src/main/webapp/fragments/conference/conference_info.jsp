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
                        <div class="card-header"><h5><fmt:message key="label.info"/></h5></div>
                        <div class="card-body">
                            <p><fmt:message key="label.when"/>: {{ eventDateTime }}</p>
                            <p><fmt:message key="label.where"/>: {{ eventAddress }}</p>
                        </div>
                    </div>

                    <div class="card" style="flex: 1; margin-left: 10px; margin-right: 10px">
                        <div class="card-header"><h5><fmt:message key="label.reports"/></h5></div>
                        <div class="card-body">
                            <ul>
                                <li ng-repeat="report in reports">
                                    {{report.topic}} -
                                    <a href="app/users#!/{{report.speaker.id}}">
                                        {{report.speaker.name}} {{report.speaker.surname}}
                                    </a>
                                </li>
                            </ul>
                            <button type="button"
                                    class="btn btn-success"
                                    data-toggle="modal"
                                    data-target="#proposeModal"
                                    ng-click="prepareToProposeReport()">
                                <fmt:message key="button.propose.report"/>
                            </button>
                        </div>
                    </div>

                    <div class="card" style="flex: 1; margin-left: 10px">
                        <div class="card-header"><h5><fmt:message key="label.description"/></h5></div>
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
                    <div class="card-header"><h5><fmt:message key="label.registered.guests"/></h5></div>
                    <div class="card-body">
                        <ol>
                            <li ng-repeat="user in registeredGuests">{{user.login}}</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>


        <div class="modal fade" id="proposeModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="label.report.proposition"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form style="margin-bottom: 30px" name="form_name" autocomplete="off" novalidate ng-submit="sendForm(form)">
                            <div class="form-group">
                                <label id="inputTopicLabel" for="topicInput"><fmt:message key="label.topic"/></label>
                                <input type="text"
                                       class="form-control"
                                       id="topicInput"
                                       placeholder="Topic"
                                       required
                                       ng-model="form.topic">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <fmt:message key="button.close"/>
                        </button>
                        <button type="submit" class="btn btn-success" ng-disabled="form.$invalid"
                                ng-click="proposeReport()"
                                data-dismiss="modal">
                            <fmt:message key="button.propose.report"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>