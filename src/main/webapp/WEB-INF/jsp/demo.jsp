<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ajax</title>
    <script type="text/javascript" src="../../script/jquery/jquery-3.2.1.min.js"></script>
</head>
<body>
<input id="username" name="username"/><br/><br/>
<input id="age" name="age"/><br/><br/>
<input id="sex" name="sex"/><br/><br/>
<button id="button" type="button">提交</button>
<br>
<hr>
<br>
<div id="div">
    <table>
        <thead>
        <tr>
            <th>username</th>
            <th>age</th>
            <th>sex</th>
        </tr>
        </thead>
        <tbody id="userlist">
        </tbody>
    </table>
</div>

<script type="text/javascript">
    $(function () {
        $("#button").click(function () {
            gogogo();
        });
    });

    function gogogo() {
        var username = $.trim($("#username").val());
        var age = $.trim($("#age").val());
        var sex = $.trim($("#sex").val());
        $.ajax({
            type: "post",
            url: "/user/demo.do",
            dataType: "json",
            async: true,
            data: {username:username, age:age, sex:sex},
            success: function (data) {
                var html = "";
                html += "<tr>" +
                        "<td>" + format(data.name) + "</td>" +
                        "<td>" + format(data.age) + "</td>" +
                        "<td>" + format(data.sex) + "</td>";
                $("#userlist").html(html);
            },
            error: function () {
                alert("failed");
            }
        });
    }

    function format(data) {
        if (data === undefined) {
            return "";
        } else {
            return data;
        }
    }

</script>

</body>
</html>
