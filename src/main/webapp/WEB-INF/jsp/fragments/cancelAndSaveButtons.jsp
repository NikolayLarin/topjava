<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
    <span class="fa fa-close"></span>
    <spring:message code="common.cancel"/>
</button>
<button type="button" class="btn btn-primary" onclick="save()">
    <span class="fa fa-check"></span>
    <spring:message code="common.save"/>
</button>