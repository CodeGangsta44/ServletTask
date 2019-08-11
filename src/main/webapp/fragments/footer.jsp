<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="messages"/>
<div class="card" style="margin-top: 20px">
    <div class="card-footer">
        Creator: Dovhopoliuk Roman
        <span style="float: right">
                        <a href="?lang=en"><fmt:message key="language.en"/></a>
                        |
                        <a href="?lang=ua"><fmt:message key="language.ua"/></a>
        </span>
    </div>
</div>