<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head/>
<body>
<form method="POST" action='meals' name="frmAddMeal">
    <h1>${not empty meal.id ? 'Edit Meal' : 'Add Meal'}</h1>
    <div style="width:500px; border-top:2px solid #9EC1D4; padding-left:10px"></div>

    <input type="hidden" readonly="readonly" name="mealId" value="${meal.id}"/><br/>
    Date and time : <input type="datetime-local" name="dateTime" value="${meal.dateTime}"/><br/>
    Description : <input type="text" name="description" value="${meal.description}"/><br/>
    Calories : <input type="number" name="calories" value="${not empty meal.calories ? meal.calories : 0}"/><br/>
    <input type="submit" value="Submit"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>