package UnitTest;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpNotFoundException;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * This class tests the HTML Pages and servlets
 */
public class WebTest extends TestCase {

    private WebConversation     webConversation = new WebConversation();
    private static final String URL_ROOT        = "http://localhost:8080/LIL-G3-appliweb";

    public WebTest( final String s ) {
        super( s );
    }

    public static TestSuite suite() {
        return new TestSuite( WebTest.class );
    }

    /**
     * try getting a response for the given Conversation and Request show an
     * error message if a 404 error appears
     * 
     * @param conversation
     *            - the conversation to use
     * @param request
     * @return the response
     * @throws an
     *             Exception if getting the response fails
     */
    public WebResponse tryGetResponse( WebConversation conversation, WebRequest request ) throws Exception {
        WebResponse response = null;
        try {
            response = conversation.getResponse( request );
        } catch ( HttpNotFoundException nfe ) {
            System.err.println( "The URL '" + request.getURL() + "' is not active any more" );
            throw nfe;
        }
        return response;
    }

    // ==================================
    // = Test cases =
    // ==================================

    /**
     * Checks that all servlets are deployed
     */
    public void testWebCheckServlets() {

        try {
            webConversation.getResponse( URL_ROOT + "/EditerClient" );
        } catch ( Exception e ) {
            fail( "The EditerClient servlet hasn't been found" );
        }
    }

    /**
     * Verifies that the welcome page has exactly one form, with the single
     * parameter, "name"
     **/
    public void testWelcomePage() throws Exception {
        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( URL_ROOT + "/Accueil" );
        WebResponse response = tryGetResponse( conversation, request );

        WebForm forms[] = response.getForms();
        assertEquals( 1, forms.length );
        assertEquals( 1, forms[0].getParameterNames().length );
        assertEquals( "name", forms[0].getParameterNames()[0] );
    }

    /**
     * Checks valid edition de client
     */
    public void testValidEditerClient() throws Exception {
        WebResponse page;
        page = webConversation.getResponse( URL_ROOT + "/EditerClient" );
        WebForm form = page.getFormWithID( "myform" );
        /*
         * form.setParameter( "motdepasse", "lk789456*" ); form.setParameter(
         * "confirmation", "lk789456*" ); // Submits the form form.submit(); //
         * After clicking the submit button // the page should be the welcome
         * page WebResponse newPage = webConversation.getCurrentPage(); String
         * title = newPage.getTitle(); boolean isNewPage = ( title.indexOf(
         * "welcome.jsp" ) != -1 ); assertTrue(
         * "The page displayed after a valid login should be the welcome page",
         * isNewPage );
         */
    }

    /**
     * Checks invalid login
     */
    public void testBadLogin() throws Exception {
        WebResponse page;
        page = webConversation.getResponse( URL_ROOT + "/betterLogin.jsp" );
        WebForm form = page.getFormWithName( "loginform" );
        form.setParameter( "login", "zzz" );
        form.setParameter( "password", "zzz" );
        // Submits the form
        form.submit();
        // After clicking the submit button
        // the page should not be the welcome page
        WebResponse newPage = webConversation.getCurrentPage();
        String title = newPage.getTitle();
        boolean isNewPage = ( title.indexOf( "welcome.jsp" ) != -1 );
        assertFalse( "The page displayed after a bad login should not be the welcome page", isNewPage );
    }
}
