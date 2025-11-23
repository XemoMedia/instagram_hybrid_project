

/*INSERT INTO email_template (template_name, subject, body)
VALUES (
    'TOKEN_GEN_MAIL',
    'Welcome to Xemo Media',
    '<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Welcome Email</title>
<style>
body {
  font-family: Arial, Helvetica, sans-serif;
  background-color: #f5f7fa;
  margin: 0;
  padding: 0;
}
.email-container {
  max-width: 600px;
  background: #ffffff;
  margin: 20px auto;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
h2 {
  color: #333333;
  margin-bottom: 10px;
}
p {
  color: #555555;
  font-size: 15px;
  line-height: 1.6;
}
.btn {
  display: inline-block;
  padding: 12px 20px;
  background-color: #4c6ef5;
  color: #ffffff !important;
  text-decoration: none;
  border-radius: 6px;
  font-size: 16px;
  margin-top: 20px;
}
.footer {
  text-align: center;
  margin-top: 30px;
  color: #999999;
  font-size: 12px;
}
</style>
</head>
<body>
<div class="email-container">
<h2>Hello, {{userName}}</h2>
<p>
Welcome to our platform! We''re excited to have you here.<br>
Please click the button below to continue:
</p>
<a href="{{link}}" class="btn">Click Here</a>
<p>If the button doesn’t work, copy and paste this link into your browser:</p>
<p style="word-break: break-all; color:#4c6ef5;">{{link}}</p>
<div class="footer">
© 2025 Xemo Media. All rights reserved.
</div>
</div>
</body>
</html>'
);




INSERT INTO email_config (
    id,
    host,
    port,
    username,
    password,
    smtp_auth,
    starttls_enable
) VALUES (
    1,
    'smtp.gmail.com',
    587,
    'xemomedia7@gmail.com',
    'svpqyzvdhxbacdhs',
    true,
    true
);*/
