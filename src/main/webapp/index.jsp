<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>index</title>
</head>
<body>
<form method="post" action="user/showAll.do">
    <input type="submit" name="查看全部用户信息" />
</form>
<form method="post" action="user/showUserById.do">
    id : <input type="text" name="id" />
    <input type="submit" name="查看以上id用户信息">
</form>
<form method="post" action="user/showUserByEmail.do">
    email : <input type="text" id="email" name="email" />
    <input type="submit" name="查看以上id用户信息">
</form>

<a href="user/toDemo.do">ajax demo 页</a>

<script>

</script>

</body>
</html>
