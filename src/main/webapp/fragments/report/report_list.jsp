<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="panel panel-default">
    <div class="panel-heading">Report requests</div>
    <div class="panel-body">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>Topic</th>
                <th>Speaker</th>
                <th>Conference</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="report in reports | orderBy: 'id'">
                <td>{{report.id}}</td>
                <td>{{report.topic}}</td>
                <td>{{report.speaker.surname}} {{report.speaker.name}}</td>
                <td>{{report.conference.topic}}</td>

                <td class="align-middle">
                    <div class="btn-group" role="group" aria-label="Basic example">
                        <button type="button"
                                class="btn btn-secondary"
                                data-toggle="modal"
                                data-target="#editModal"
                                ng-click="editReport(report)">
                            <fmt:message key="button.edit"/>
                        </button>
                        <button class="btn btn-danger"
                                type="button"
                                data-toggle="modal"
                                data-target="#deleteModal"
                                ng-click="prepareToDeleteReport(report.id)">
                            <fmt:message key="button.delete"/>
                        </button>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>

        <!-- EditModal -->
        <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="ModalLabelEditReport">Edit Report</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="alert alert-success" role="alert" id="resultMessage" style="visibility: hidden"></div>
                        <div class="form-group">
                            <label id="inputSurnameLabel" for="exampleInputSurname"><fmt:message key="label.topic"/></label>
                            <input type="text"
                                   class="form-control"
                                   id="exampleInputSurname"
                                   placeholder="<fmt:message key="label.topic"/>"
                                   required
                                   ng-model="editForm.topic">

                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <fmt:message key="button.close"/>
                        </button>
                        <button class="btn btn-success" ng-click="saveChanges()">
                            <fmt:message key="button.save"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- DeleteModal -->
        <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="ModalLabelConfirmDeleting">Confirm deleting</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"
                                ng-click="cancelReportDeleting()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Are you sure?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary"
                                data-dismiss="modal"
                                ng-click="cancelReportDeleting()">
                            <fmt:message key="button.cancel"/>
                        </button>
                        <button class="btn btn-danger" data-dismiss="modal" ng-click="deleteReport()">
                            <fmt:message key="button.delete"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>