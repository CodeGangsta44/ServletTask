<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<link rel="stylesheet" type="text/css" href="../../css/user_list.css">
<div class="panel panel-default">
    <div class="panel-heading"><fmt:message key="table.all_users.name"/></div>
    <div class="panel-body">
        <table class="table table-striped">
            <thead>
            <tr>
                <th><fmt:message key="column.id"/></th>
<%--                <th><fmt:message key="label.photo"/></th>--%>
                <th><fmt:message key="label.surname"/></th>
                <th><fmt:message key="label.name"/></th>
                <th><fmt:message key="label.patronymic"/></th>
                <th><fmt:message key="label.login"/></th>
                <th><fmt:message key="label.email"/></th>
                <th><fmt:message key="label.roles"/></th>
                <th><fmt:message key="label.actions"/></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="item in users | orderBy:'id'">
                <td class="align-middle">{{item.id}}</td>
<%--                <td class="align-middle">--%>
<%--                    <img ng-src="/img/{{item.avatarFileName}}" alt=""--%>
<%--                         style="object-fit: cover; height: 50px; width: 50px; border-radius:50%">--%>
<%--                </td>--%>
                <td class="align-middle">{{item.surname}}</td>
                <td class="align-middle">{{item.name}}</td>
                <td class="align-middle">{{item.patronymic}}</td>
                <td class="align-middle">{{item.login}}</td>
                <td class="align-middle">{{item.email}}</td>

                <td class="align-middle">
                    {{item.roles.join(" | ")}}
                </td>

                <td class="align-middle">
                    <div class="btn-group" role="group" aria-label="Basic example">
                        <button type="button"
                                class="btn btn-secondary"
                                data-toggle="modal"
                                data-target="#editModal"
                                ng-click="editUser(item)">
                            <fmt:message key="button.edit"/>
                        </button>
                        <button type="button"
                                class="btn btn-secondary"
                                data-toggle="modal"
                                data-target="#proposeReportModal"
                                ng-click="prepareToProposeReport(item)">
                            <fmt:message key="button.propose.report"/>
                        </button>
                        <button class="btn btn-danger"
                                type="button"
                                data-toggle="modal"
                                data-target="#deleteModal"
                                ng-click="prepareToDeleteUser(item.id)">
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
                        <h5 class="modal-title" id="ModalLabelEditProfile">Edit Profile</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="alert alert-success" role="alert" id="resultMessage" style="visibility: hidden"></div>
                        <div class="form-group">
                            <label id="inputSurnameLabel" for="exampleInputSurname"><fmt:message key="label.surname"/></label>
                            <input type="text"
                                   class="form-control"
                                   id="exampleInputSurname"
                                   placeholder="<fmt:message key="label.surname"/>"
                                   required
                                   ng-model="editForm.surname">

                        </div>
                        <div class="form-group">
                            <label id="inputNameLabel" for="exampleInputName"><fmt:message key="label.name"/></label>
                            <input type="text"
                                   class="form-control"
                                   id="exampleInputName"
                                   placeholder="<fmt:message key="label.name"/>"
                                   required
                                   ng-model="editForm.name">
                        </div>
                        <div class="form-group">
                            <label id="inputPatronymicLabel" for="exampleInputPatronymic"><fmt:message key="label.patronymic"/></label>
                            <input type="text"
                                   class="form-control"
                                   id="exampleInputPatronymic"
                                   placeholder="<fmt:message key="label.patronymic"/>"
                                   required
                                   ng-model="editForm.patronymic">
                        </div>
                        <div class="form-group">
                            <label id="inputLoginLabel" for="exampleInputLogin"><fmt:message key="label.login"/></label>
                            <input type="text"
                                   class="form-control"
                                   id="exampleInputLogin"
                                   placeholder="<fmt:message key="label.login"/>"
                                   required
                                   ng-model="editForm.login">
                        </div>
                        <div class="form-group">
                            <label id="inputEmailLabel" for="exampleInputEmail"><fmt:message key="label.email"/></label>
                            <input type="text"
                                   class="form-control"
                                   id="exampleInputEmail"
                                   placeholder="<fmt:message key="label.email"/>"
                                   required
                                   ng-model="editForm.email">
                        </div>

                        <div class="form-group">
                            <label id="inputRolesLabel"><fmt:message key="label.roles"/></label>
                            <ul ng-repeat="role in roles" style="list-style: none; padding-left: 0">
                                <li>
                                    <input type="checkbox"
                                           name="selectedRoles[]"
                                           value="{{role}}"
                                           ng-checked="editForm.roles.indexOf(role) > -1"
                                           ng-click="toggleRoleSelection(role)"> {{role}}
                                </li>
                            </ul>
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
                                ng-click="cancelUserDeleting()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        Are you sure?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary"
                                data-dismiss="modal"
                                ng-click="cancelUserDeleting()">
                            <fmt:message key="button.cancel"/>
                        </button>
                        <button class="btn btn-danger" data-dismiss="modal" ng-click="deleteUser()">
                            <fmt:message key="button.delete"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- ProposeReportModal -->
        <div class="modal fade" id="proposeReportModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="ModalLabelReportProposing">Report proposing</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"
                                ng-click="cancelUserDeleting()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form name="form_name" autocomplete="off" novalidate ng-submit="sendForm(form)">
                            <div class="form-group">

                                <div class="form-group">
                                    <label id="inputConferenceLabel" for="conferenceSelect">Conference</label>
                                    <select class="form-control"
                                            name="conferenceSelect"
                                            id="conferenceSelect"
                                            ng-model="chosenConferenceId">
                                        <option ng-repeat="conference in conferences | orderBy: id " value="{{conference.id}}">
                                            {{conference.topic}}
                                        </option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label id="inputTopicLabel" for="topicInput">Topic</label>
                                    <input type="text"
                                           class="form-control"
                                           id="topicInput"
                                           placeholder="Topic"
                                           required
                                           ng-model="reportProposeForm.topic">
                                </div>
                            </div>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary"
                                data-dismiss="modal"
                                ng-click="cancelUserDeleting()">
                            <fmt:message key="button.cancel"/>
                        </button>
                        <button class="btn btn-success" data-dismiss="modal" ng-click="proposeReport()">
                            <fmt:message key="button.propose.report"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>


    </div>
</div>