<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="container" style="margin-top: 30px">
    <div style="display: flex; margin-top: 20px">
<%--        <div style="flex: 1;  align-self:flex-start">--%>
<%--            <button type="button" data-toggle="modal" data-target="#avatarModal" style="background-color: transparent; border: none">--%>
<%--                <img ng-src="/img/{{user.avatarFileName}}" alt=""--%>
<%--                     style="object-fit: cover; height: 350px; width: 350px; border-radius:50%">--%>
<%--            </button>--%>
<%--        </div>--%>

        <div class="card" style="flex: 1; align-self:flex-start">
            <div class="card-header" >
                <div style="display: inline-block"><h5>Info</h5></div>
                <button type="button" class="btn btn-success"  data-toggle="modal" data-target="#editModal"
                        style="float: right"><fmt:message key="button.edit"/></button>
            </div>
            <div class="card-body">
                <div>
                    <label id="surnameLabel"><fmt:message key="label.surname"/>: {{user.surname}} </label>
                </div>

                <div>
                    <label id="nameLabel"><fmt:message key="label.name"/>: {{user.name}}</label>
                </div>

                <div>
                    <label id="patronymicLabel"><fmt:message key="label.patronymic"/>: {{user.patronymic}}</label>
                </div>

                <div>
                    <label id="loginLabel"><fmt:message key="label.login"/>: {{user.login}}</label>
                </div>

                <div>
                    <label id="emailLabel"><fmt:message key="label.email"/>: {{user.email}}</label>
                </div>
            </div>
        </div>
    </div>

    <!-- AvatarModal -->
    <div class="modal fade" id="avatarModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Change photo</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <input type="file"  file="file" required>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button class="btn btn-success" ng-click="changeAvatar()">
                        Upload
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- EditModal -->
    <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Edit Profile</h5>
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
                               ng-model="user.surname">

                    </div>
                    <div class="form-group">
                        <label id="inputNameLabel" for="exampleInputName"><fmt:message key="label.name"/></label>
                        <input type="text"
                               class="form-control"
                               id="exampleInputName"
                               placeholder="<fmt:message key="label.name"/>"
                               required
                               ng-model="user.name">
                    </div>
                    <div class="form-group">
                        <label id="inputPatronymicLabel" for="exampleInputPatronymic"><fmt:message key="label.patronymic"/></label>
                        <input type="text"
                               class="form-control"
                               id="exampleInputPatronymic"
                               placeholder="<fmt:message key="label.patronymic"/>"
                               required
                               ng-model="user.patronymic">
                    </div>
                    <div class="form-group">
                        <label id="inputLoginLabel" for="exampleInputLogin"><fmt:message key="label.login"/></label>
                        <input type="text"
                               class="form-control"
                               id="exampleInputLogin"
                               placeholder="<fmt:message key="label.login"/>"
                               required
                               ng-model="user.login">
                    </div>
                    <div class="form-group">
                        <label id="inputEmailLabel" for="exampleInputEmail"><fmt:message key="label.email"/></label>
                        <input type="text"
                               class="form-control"
                               id="exampleInputEmail"
                               placeholder="<fmt:message key="label.email"/>"
                               required
                               ng-model="user.email">
                    </div>

                    <div class="form-group">
                        <label id="inputIsSpeakerLabel" for="exampleInputIsSpeaker"><fmt:message key="label.speaker"/></label>
                        <input type="checkbox"
                               class="form-control"
                               id="exampleInputIsSpeaker"
                               placeholder="<fmt:message key="label.speaker"/>"
                               ng-model="isSpeaker"
                               ng-checked="isSpeaker"
                               ng-click="toggleSpeakerSelection()">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button class="btn btn-success" ng-click="editProfile()">
                        Save
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="card" style="flex: 1; margin-top: 20px">
        <div class="card-header"><h4><fmt:message key="label.roles"/></h4></div>
        <div class="card-body">
            <div>
                <ul style="list-style: none; padding-left: 0">
                    <li ng-repeat="role in user.roles">
                        <div class="alert alert-success" role="alert">
                            {{role}}
                        </div>
                    </li>
                </ul>

            </div>

        </div>
    </div>
</div>
