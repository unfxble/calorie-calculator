<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style>
    .green {
        color: green;
    }

    .red {
        color: red;
    }
</style>


<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<div style="width:500px; border-top:2px solid #9EC1D4; padding-left:10px"></div>
<h1>Meals</h1>
<p><a href="meals?action=add">Add meal</a></p>
<table border="1">
    <thead>
    <%--        <th>Id</th>--%>
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
    <th colspan="2">Action</th>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <tr class=<c:out value="${meal.excess == true ? 'red' : 'green'}"/>>
            <%--<td><c:out value="${meal.id}"/></td>--%>
            <td><c:out value="${meal.dateTime.format(DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\"))}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>