<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="container" style="margin-top: 20px">
    <div style="display: flex">
        <nav aria-label="Page navigation example" style="flex: 1">
            <ul id="topPageButtonGroup" class="pagination form-inline" style="float: left" hidden>
                <li class="page-item previous-page-button">
                    <a class="page-link" href="" ng-click="previousPage()">Previous</a>
                </li>
                <li id="pageButton-top-{{$index + 1}}" class="page-item" ng-repeat="x in [].constructor(numberOfPages) track by $index">
                    <a class="page-link" href="" ng-click="selectPage($index + 1)">{{$index + 1}}</a>
                </li>
                <li class="page-item next-page-button">
                    <a class="page-link" href="" ng-click="nextPage()">Next</a>
                </li>
            </ul>
            <div class="form-inline" style="float: right">
                <label class="my-1 mr-2" for="TopInlineFormCustomSelect" >Items on page: </label>
                <select class="custom-select my-1 mr-sm-2" id="TopInlineFormCustomSelect"
                        ng-model="currentCapacity"
                        ng-change="updateButtons()">
                    <option selected>All</option>
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="15">15</option>
                </select>
            </div>
        </nav>
    </div>
    <div class="panel-body">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>Topic</th>
                <th>Date and Time</th>
                <th>Address</th>
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
    <div style="display: flex">
        <nav aria-label="Page navigation example" style="flex: 1">
            <ul id="bottomPageButtonGroup" class="pagination form-inline" style="float: left" hidden>
                <li class="page-item previous-page-button">
                    <a class="page-link" href="" ng-click="previousPage()">Previous</a>
                </li>
                <li id="pageButton-bottom-{{$index + 1}}" class="page-item" ng-repeat="x in [].constructor(numberOfPages) track by $index">
                    <a class="page-link" href="" ng-click="selectPage($index + 1)">{{$index + 1}}</a>
                </li>
                <li class="page-item next-page-button">
                    <a class="page-link" href="" ng-click="nextPage()">Next</a>
                </li>
            </ul>
            <div class="form-inline" style="float: right">
                <label class="my-1 mr-2" for="TopInlineFormCustomSelect" >Items on page: </label>
                <select class="custom-select my-1 mr-sm-2" id="TopInlineFormCustomSelect"
                        ng-model="currentCapacity"
                        ng-change="updateButtons()">
                    <option selected>All</option>
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="15">15</option>
                </select>
            </div>
        </nav>
    </div>
</div>