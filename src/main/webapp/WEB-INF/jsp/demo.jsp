<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ajax</title>
    <script type="text/javascript" src="../../script/jquery/jquery-3.2.1.min.js"></script>
</head>
<body>
    <input id="username" name="username"/><br/><br/>
    <input id="age" name="age"/><br/><br/>
    <input id="sex" name="sex" /><br/><br/>
    <button id="button" type="button">提交</button>
<br>
<hr>
<br>
<div id="div"><span id="span"> </span></div>

<script type="text/javascript">
    $(function() {
        $("#span").html("haha");
        $("#button").click(function () {
            gogogo();
        });
    });

    function gogogo() {
        $("#span").html("hehe");
        var username = $("#username").val();
        var age = $("#age").val();
        var sex = $("#sex").val();
        $.ajax({
            type: "post",
            url: "/user/demo.do",
//            dataType: "json",
            async: true,
            data: {username:username},
            success: function (data) {
                alert("success" + data);
            },
            error: function () {
                alert("failed");
            }
        });
    }
</script>

</body>
</html>
