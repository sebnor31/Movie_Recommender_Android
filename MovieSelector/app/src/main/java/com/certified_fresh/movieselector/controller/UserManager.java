package com.certified_fresh.movieselector.controller;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.certified_fresh.movieselector.model.Admin;
import com.certified_fresh.movieselector.model.Student;
import com.certified_fresh.movieselector.model.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by nsebkhi3 on 2/10/2016.
 */
public final class UserManager {

    private static Context context;
    private static List<User> userList;
    public static User currentUser;


    public static void init(Context appContext) throws IOException {

        context = appContext;

        userList = new ArrayList<User>();

        File userFile = new File(context.getFilesDir(), "userData.xml");

        // Create user database if does not exist yet
        if (!userFile.exists()) {
            FileOutputStream userDataFile  = context.openFileOutput("userData.xml", Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "user");
            xmlSerializer.endTag(null,"user");
            xmlSerializer.endDocument();

            userDataFile.write(writer.toString().getBytes());
            userDataFile.close();
            Log.i("Fresh", "User XML created!");
            return;
        }

        // Populate list of users
        setUserList();
    }

    private static void setUserList() throws IOException {

        // Remove all users in current list
        userList.clear();

        // Initiate an xml parser
        FileInputStream userDataFile  = context.openFileInput("userData.xml");
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(userDataFile, null);

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {

                parser.next();

                if (parser.getEventType() != XmlPullParser.START_TAG || parser.getName().equals("user")) {
                    continue;
                }

                // Add user to list
                ArrayList<String> userArgs = new ArrayList<String>();
                int attCount = parser.getAttributeCount(); // number of args for user constructor

                for(int i = 0 ; i < attCount; i++) {
                    String arg = parser.getAttributeValue(i);
                    userArgs.add(arg);
                }

                if (parser.getName().equals("student")) {
                    userList.add(new Student(userArgs));
                }
                else if (parser.getName().equals("admin")){
                    userList.add(new Admin(userArgs));

                } else {
                    Log.e("Fresh", "User is not recognized. Cannot add.");
                }
            }
        }
        catch (XmlPullParserException e) {
            Log.e("Fresh", "User xml cannot be read");
        }
        catch(Exception e) {
            Log.e("Fresh", e.getMessage());
        }
        finally {
            userDataFile.close();
        }
    }

    public static void readUserList() {
        for (User user: userList) {
            String userInfo;

            if(user.getType().equals("s")) {
                userInfo = String.format("%s\t%s\t%s %s\t%s\t%s\t%s\t%s", user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), ((Student)user).getMajor(), ((Student)user).getInterest(), user.getType(), user.getEmail() );
            }
            else {
                userInfo = String.format("%s\t%s\t%s %s\t%s\t%s", user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getType(), user.getEmail() );
            }

            Log.d("Fresh", userInfo);
        }
    }

    public static void addUser(User user) {

        try {
            File userFile = new File(context.getFilesDir(), "userData.xml");

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(userFile);
            Node root = document.getElementsByTagName("user").item(0);

            // append a new node to the first employee
            if (user.getType().equals("s")){
                Element userElt = document.createElement("student");
                root.appendChild(userElt);

                userElt.setAttribute("firstName", user.getFirstName());
                userElt.setAttribute("lastName", user.getLastName());
                userElt.setAttribute("username", user.getUsername());
                userElt.setAttribute("password", user.getPassword());
                userElt.setAttribute("email", user.getEmail());
                userElt.setAttribute("type", user.getType());
                userElt.setAttribute("major", "CS");
                userElt.setAttribute("interest", "none");
            }
            else {
                Element userElt = document.createElement("admin");
                root.appendChild(userElt);

                userElt.setAttribute("firstName", user.getFirstName());
                userElt.setAttribute("lastName", user.getLastName());
                userElt.setAttribute("username", user.getUsername());
                userElt.setAttribute("password", user.getPassword());
                userElt.setAttribute("email", user.getEmail());
                userElt.setAttribute("type", user.getType());
            }

            // write the DOM object to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(userFile);
            transformer.transform(domSource, streamResult);

        }
        catch(Exception e) {
            Log.e("Fresh", e.getMessage());
        }

        // Add user to list
        userList.add(user);

        Student newUser = (Student)userList.get(userList.size()-1);
        String debug = String.format("Added user - interest: %s\nmajor: %s", newUser.getInterest(), newUser.getMajor());
        Log.d("Fresh", debug);
    }

    public static int authenticate(String username, String password) {
        int verifiedUser = 0;

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);

            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {

                // Return 1 if user is a student, otherwise 2 if admin
                verifiedUser = (user.getType().equals("s")) ? 1 : 2;

                // Set current user
                currentUser = user;

                // Put current user at end of the list
                userList.remove(i);
                userList.add(currentUser);

                // Get out of loop as user has been found
                break;
            }
        }

        return verifiedUser;
    }

    public static void updateUserProfile(List<String> args) {
        // Update temporary user
        currentUser.updateUser(args);

        // Update user database
        try {
            File userFile = new File(context.getFilesDir(), "userData.xml");

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(userFile);

            NodeList students = document.getElementsByTagName("student");

            for (int i = 0; i < students.getLength(); i++){

                Node student = students.item(i);
                String usernameAttr = ((Element) student).getAttribute("username");

                if (usernameAttr.equals(currentUser.getUsername())) {
                    ((Element) student).setAttribute("firstName", currentUser.getFirstName());
                    ((Element) student).setAttribute("lastName", currentUser.getLastName());
                    ((Element) student).setAttribute("password", currentUser.getPassword());
                    ((Element) student).setAttribute("email", currentUser.getEmail());
                    ((Element) student).setAttribute("major", ((Student)currentUser).getMajor());
                    ((Element) student).setAttribute("interest", ((Student)currentUser).getInterest());

                    break;
                }
            }

            // write the DOM object to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(userFile);
            transformer.transform(domSource, streamResult);
        }
        catch(Exception e) {
            Log.e("Fresh", e.getMessage());
        }
    }

}
