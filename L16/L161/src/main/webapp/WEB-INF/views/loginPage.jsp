<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="UTF-8"/>
  <title>Login</title>
  <link type="text/css" href="resources/background.css" rel="stylesheet"/>
</head>
<body>
<div class="top">
  <img src="resources/logo.png"/>
  <br/>
</div>
<div class="login">
  <form action="/login" method="POST">
    login: <input type="text" name="login"/>
    <input type="submit" value="submit">
  </form>
  <p>Previous login: ${login}</p>
  <a href="monitoring">Monitoring</a>
  <a href="orm">ORM</a>
</div>
</body>
</html>
