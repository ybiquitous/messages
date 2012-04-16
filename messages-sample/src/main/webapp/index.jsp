<%@ page import="message.MessageKeys" %>
<!doctype html>
<html lang="${lang}">
<head></head>
<body>
<%= MessageKeys.test_key.get() %><br>
<%= MessageKeys.test_key.get(1,2) %><br>
<%= MessageKeys.test_key.get(new java.math.BigDecimal(142342424200.34), new java.util.Date()) %><br>
</body>
</html>
