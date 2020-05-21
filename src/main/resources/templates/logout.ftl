<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8"/>
<meta name="unauthorized" content="true"/>
<title>Byteflair Authorization Server</title>
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
</head>
<body>
    <form id="logoutForm" action="/logout" method="post" >
      <input id="logout_submit" class="rc-button rc-button-submit" type="submit" value="Logout" name="logout"/>
      <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</body>
</html>