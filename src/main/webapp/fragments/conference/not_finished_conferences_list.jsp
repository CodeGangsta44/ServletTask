<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="panel panel-default">
    <div class="panel-heading"><fmt:message key="label.not.finished.conferences"/></div>
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
                    <button type="button"
                            class="btn btn-secondary"
                            data-toggle="modal"
                            data-target="#finishModal"
                            ng-click="prepareForConferenceFinishing(item.id)">
                        <fmt:message key="button.finish"/>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>

        <!-- FinishModal -->
        <div class="modal fade" id="finishModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel"><fmt:message key="label.conference.finishing"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"
                                ng-click="cancelConferenceFinishing()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label id="inputNumberOfVisitedGuestsLabel" for="exampleInputNumberOfVisitedGuests"><fmt:message key="label.name"/></label>
                            <input type="text"
                                   class="form-control"
                                   id="exampleInputNumberOfVisitedGuests"
                                   placeholder="<fmt:message key="label.attended.guests"/>"
                            required
                            ng-model="numberOfVisitedGuests">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary"
                                data-dismiss="modal"
                                ng-click="cancelConferenceFinishing()">
                            <fmt:message key="button.cancel"/>
                        </button>
                        <button class="btn btn-danger" data-dismiss="modal" ng-click="finishConference()">
                            <fmt:message key="button.finish"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>