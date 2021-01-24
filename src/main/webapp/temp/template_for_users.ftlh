<#ftl encoding="UTF-8"/>
<html lang="en">
<body>

<table>
    <tr>
        <th>Id</th>
        <th>Login</th>
        <th>Delete</th>
    </tr>
    <tbody>
    <#list users as listUser>
        <#if listUser.isDeleted == false || listUser.isDeleted == null>
            <tr>
                <td>${listUser.id}</td>
                <td>${listUser.login}</td>
                <td>

                    <form action="/all?action=delete&userId=${listUser.id}" method="post">
                        <input type="submit" value="Delete">
                        <input type="hidden" value="${_csrf_token}" name="_csrf_token">
                    </form>
                </td>
            </tr>
        </#if>
    </#list>
    </tbody>
</table>


</body>
</html>
