<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<base href="${pageContext.request.contextPath}/"/>
<jsp:include page="fragments/headTag.jsp"/>
<body>

<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <hr>
    <jsp:useBean id="action" scope="request" type="java.lang.String"/>

    <spring:message var="create" code="meal.create"/>
    <spring:message var="update" code="meal.update"/>

    <h3>${action == 'create' ? create : update}</h3>

    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals/">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.dateTime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit">
            <spring:message code="meal.save"/>
        </button>
        <button onclick="window.history.back()" type="button">
            <spring:message code="meal.cancel"/>
        </button>
    </form>
    <hr>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
