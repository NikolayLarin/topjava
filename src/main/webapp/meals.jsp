<%@ page import="ru.javawebinar.topjava.web.SecurityUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>

    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <h3>Now active user with id =
        <c:out value="<%=SecurityUtil.authUserId()%>"/></h3>
    <a href="meals?action=create">Add Meal</a>
    <br><br>

    <form method="get" action="meals">
        <p>Введите даты начала и окончания выборки</p>
        <input type="text" name="action" value="filter" hidden>
        <input type="date" name="startDate" value='${param.get("startDate")}'>
        <input type="date" name="endDate" value='${param.get("endDate")}'>
        <br/><br/>
        <p>Введите время начала и окончания выборки</p>
        <input type="time" name="startTime" value='${param.get("startTime")}'>
        <input type="time" name="endTime" value='${param.get("endTime")}'>
        <br/><br/>
        <input type="submit" value="Отфильтровать">
        <br/>
    </form>

    <form method="get" action="meals">
        <input type="submit" value="Отменить фильтрацию">
        <br/><br/>
    </form>

    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <jsp:useBean id="meals" scope="request" type="java.util.List"/>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>

</section>
</body>
</html>