<%@ page import="message.MessageKeys" %>
<!doctype html>
<html lang="${lang}">
<head></head>
<body>
<h1>lang=${lang}</h1>
<ul>
<li><%= MessageKeys.test_key.get() %></li>
<li><%= MessageKeys.test_key.get(1,2) %></li>
<li><%= MessageKeys.test_key.get(new java.math.BigDecimal(142342424200.34), new java.util.Date()) %></li>
</ul>
</body>
</html>
