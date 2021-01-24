<#ftl encoding="UTF-8"/>
<html>
<head>
    <title>Registration</title>
</head>
<body>

<H1>Registration page</H1>

<form method="post" action="/registration">
    <label>Login
        <input name="login">
    </label>
    <br>
    <label>Password
        <input name="password" type="password">
    </label>
    <br>
    <input type="hidden" value="${_csrf_token}" name="_csrf_token">
    <button type="submit">Register</button>
</form>
</body>
</html>