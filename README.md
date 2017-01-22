# Cyber Security Base - Course project I
This spring web-app has five vulnerabilities from the OWASP 2013 top ten list.

Source code available at: https://github.com/samusarivaara/cybersecuritybase-project

## 1. Vulnerability #1 OWASP 2013-A1-Injection

An authenticated user can inject SQL through nameHint form parameter.

### How to find it?
	1. Go to http://localhost:8080/, you will be redirected to the /login-page. Use credentials fellow/fellow to login.
	2. On the main page (/form), you will see three actions. Choose action 1 "Sign up to the event using this form" by
      entering Name and Address and clicking "submit" button. Use prefix (username) on Name and Address fields,
	  so you can later identify the submitter (user) of this data. Example Name: fellow_my_name Address: fellow_my_address.
	3. You will see a summary page (/done/fellow). Click "Goo Back" to return to the main page.
	4. Optional - Go back to Step 2 and add execute more signups, remember to prefix them correctly.
	5. Logout by using link: "Shortcut to log out".
	6. Login as different user. Use credentials: fellow2, fellow2.
	7. On main page (/form) Choose action 3. "Check my sign ups by name" by entering SQL injection into the field Name.
      Underlying database has a column called "name", so you can use this string "' OR 1=1 OR name='a" in a form to see
	  all names in the Sign up database.

### How to fix it?
	1. Go to source sec.project.repository.SignupRepositoryImpl.java and implement parameter binding according
      http://www.thoughts-on-java.org/jpa-native-queries/
	  Hint: Comment the line which starts with "Query ..." and use the source that is commented under "Fix me".
   // Query query = em.createNativeQuery("SELECT name from Signup WHERE owner = ? AND name = ?");
   // query.setParameter(1, owner);
   // query.setParameter(2, name);

## 2. Vulnerability #2 2013-A2-Broken Authentication and Session Management

A custom logout page is "defined" but it does not invalidate the session (ID).

### How to find it?
	1. Go to http://localhost:8080, login with credentials fellow/fellow.
	2. On main page (/form) do sign up (ie. Name: foo Address: bar) by using action 1.
	4. Sig out by using main page's link "Log out". Note: There are two types of logout links in this page.
	5. You will see a custom logout page "You are now successfully logout".
	6. Type address "http://localhost:8080/form" into you browser.
	7. *Boom* -> your session was not invalidated. You can still use functions on main page.

### How to fix it?

    1. Invalidate session when custom logout is executed by calling ServletRequest.logout()

	   See solution from sec.project.controller.SignupController.java on method LogoutMapping
	   Uncomment following lines:
       //    try {
       //        httpServletRequest.logout();
       //    } catch (ServletException ex) {
       //        Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
       //        return "redirect:/logout";
       //    }

## 3. Vulnerability #3 2013-A7-Missing Function Level Access Control

Any authenticated user can access admin url.

### How to find it?
	1. Go to http://localhost:8080/admin -> you will be redirected to login page.
    2. Use admin credentials: admin/admin to login.
	3. You will see admin view with sensitive data.
	4. Logout and re-login with user level credentials: fellow/fellow
	5. Go to http://localhost:8080/admin
	6. You will see content that is not allowed for the regular user.

### How to fix it?
	1. Check whether user is admin before showing the admin page.
	2. Go to source sec.project.AdminController and fix it by adding a check
	   // if (!authorized) {
       // return "redirect:/form";
       //}
