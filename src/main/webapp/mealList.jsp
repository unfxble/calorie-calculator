<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
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
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
    <th colspan="2">Action</th>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class=${meal.excess ? 'red' : 'green'}>
            <td>${TimeUtil.toDateTimeFormat(meal.dateTime)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&mealId=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&mealId=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>