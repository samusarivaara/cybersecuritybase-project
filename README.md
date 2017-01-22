# Cyber Security Base - Course project I
This spring web-app has at least five vulnerabilities from the OWASP 2013 top ten list.
Source code is available at: https://github.com/samusarivaara/cybersecuritybase-project

## Vulnerability #1 OWASP 2013-A1-Injection

An authenticated user can inject SQL through nameHint form parameter.

### How to find it?
1. Go to http://localhost:8080/, you will be redirected to the /login-page. Use credentials
   fellow/fellow to login
2. On the main page (/form), you will see three actions. Choose action 1 "Sign up to the event
   using this form" by entering *Name* and *Address* and clicking "submit" button. Use prefix
   (username) on Name and Address fields, so you can later identify the submitter (user) of
   this data. Example Name: fellow_my_name Address: fellow_my_address
3. You will see a summary page (/done/fellow). Click "Goo Back" to return to the main page
4. Optional - Go back to Step 2 and execute more signups, remember to prefix them correctly
5. Logout by using link: "Shortcut to log out"
6. Login as different user. Use credentials: fellow2/fellow2
7. On main page (/form) Choose action 3. "Check my sign ups by name" by entering following
   string "' OR 1=1 OR name='a" into Name field and then click the submit button
8. You will see all signup names from underlying database

### How to fix it?
Find source *sec.project.repository.SignupRepositoryImpl.java* and implement parameter
binding according to http://www.thoughts-on-java.org/jpa-native-queries/

Hint: Replace the query with this block

    Query query = em.createNativeQuery("SELECT name from Signup WHERE owner = ? AND name = ?");
    query.setParameter(1, owner);
    query.setParameter(2, name);

## Vulnerability #2 2013-A2-Broken Authentication and Session Management

A custom logout page is "defined" but it does not invalidate the session (ID).

### How to find it?
1. Go to http://localhost:8080, login with credentials fellow/fellow
2. On main page (/form) do sign up (ie. Name: foo Address: bar) by using action 1
4. Sig out by using main page's link "Log out". Note: There are different of logout links in
   this page
5. You will see a custom logout page "You are now successfully logout"
6. Type address "http://localhost:8080/form" into you browser
7. *Boom* -> your session was not invalidated. You can still use the actions on the main page

### How to fix it?

Invalidate session when custom logout is executed by calling *ServletRequest.logout()*
Find source *sec.project.controller.SignupController.java* and fix *LogoutMapping()* method.

Hint: Add following block

    try {
        httpServletRequest.logout();
    } catch (ServletException ex) {
        Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        return "redirect:/logout";
    }

## Vulnerability #3 2013-A7-Missing Function Level Access Control

Any authenticated user can access the admin url.

### How to find it?
1. Go to http://localhost:8080/admin -> you will be redirected to login page
2. Use admin credentials: admin/admin to login
3. You will see admin view with sensitive data
4. Logout and re-login with user level credentials: fellow/fellow
5. Go to http://localhost:8080/admin
6. You will see the content which is only allowed for admin user

### How to fix it?

Check whether user is admin before showing the admin page.

Hint: Find source *sec.project.AdminController* and fix it by adding following check

    if (!authorized) {
      return "redirect:/form";
    }

## Vulnerability #4 2013-A4-Insecure Direct Object References

Authenticated user can access directly to other user's resource by guessing
his/her username which is a part of the path variable: */done/{userid}*.

### How to find it?
1. Go to http://localhost:8080/ -> you will be redirected to login page
2. Use credentials fellow/fellow
3. On main page (/form) do sign up (ie. Name: foo Address: bar) by using action 1
4. Once the sign up is complete you will be redirected to the summary page
   /done/fellow
5. You noticed that user id is part of the url
6. Logout
7. Login with credential fellow2/fellow2
8. Type url http://localhost:8080/done/fellow (you are guessing other user's usename)
9. You will see another user's information but you shouldn't

### How to fix it

Don't use path variable at all, fetch user info from the *Authentication* object.

Hint: Find source *sec.project.controller.SignupController.java* and add following block

    User user = (User) authentication.getPrincipal();
    if (!user.getUsername().equals(userid)) {
        return "redirect:/done/" + user.getUsername();
    }

## Vulnerability #5 2013-A10-Unvalidated Redirects and Forwards

The request parameter *url* is not validated while redirecting.

### How to find it?
1. Go to http://localhost:8080/ -> you will be redirected to login page
2. Use credentials fellow/fellow
3. Logout by using link "Shortcut to log out", you will notice that it tooks a parameter url
4. Login again with same credentials.
5. Manipulate url parameter by entering url: http://localhost:8080/shortcut?url=http://www.google.com
   into your browser
6. You are redirected to www.google.com instead of logout page.

### How to fix it

Validate that redirect is one of the allowed redirect addresses.

Hint: Find source *sec.project.controller.SignupController* and add check to *shortCutMapping()* method

    if (!"logout".equals(url)) {
        return "redirect:error";
    }

