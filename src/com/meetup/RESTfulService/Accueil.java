package com.meetup.RESTfulService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.meetup.DB.DBClass;
import com.meetup.util.ToJSON;


@Path("accueil")
public class Accueil {
	
	
	

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(String incomingData) throws Exception
    {
        JSONObject jsonObject = new JSONObject(incomingData);
        String username = jsonObject.getString("email");
        String password = jsonObject.getString("pwd");
        System.out.println("username : " + username + " password : " + password);
        Connection conn = DBClass.returnConnection();
        PreparedStatement preStatement = conn.prepareStatement("select * from users where email = ?");
        preStatement.setString(1,username );
        ResultSet rs = preStatement.executeQuery();
        rs.beforeFirst();
        
        if(rs.next())
        {
            System.out.println("Il y a rs.next() username:"+username+"password:"+password);
        
            if(rs.getString(4).equals(password))
            {
                return Response.status(200).entity("ok").build();
            }
            else 
            {
                return Response.status(400).entity("password error").build();
            }
        }
        else
        {
            return Response.status(400).entity(" user does not exist").build();
        }    
    }


	
	@POST
	@Path("signup")
	//@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signUp(String incomingData) throws Exception
	{
		JSONObject jsonObject = new JSONObject(incomingData);
        String firstnamesignup = jsonObject.getString("firstnamesignup");
        String lastnamesignup = jsonObject.getString("lastnamesignup");
        String emailsignup = jsonObject.getString("emailsignup");
        String passwordsignup = jsonObject.getString("passwordsignup");
        String biography = jsonObject.getString("biography");
        
		Connection conn = DBClass.returnConnection();
        PreparedStatement preStatement = conn.prepareStatement("insert into users(firstname,lastname,email,pwd,biography) values(?,?,?,?,?)");
        preStatement.setString(1, firstnamesignup);
        preStatement.setString(2, lastnamesignup);
        preStatement.setString(3, emailsignup);
        preStatement.setString(4, passwordsignup);
        preStatement.setString(5, biography);
		int i = preStatement.executeUpdate();
	    preStatement.close();
	    conn.close();
	    if(i>=0)
	    	return Response.status(200).entity("ok").build();
	    else 
	    	return Response.status(400).entity("errrrrrroorrrr").build();
	    	
	}
	

	
	@GET
	@Path("getGroups")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getGroups() throws Exception
    {
		//JSONObject jsonObject = new JSONObject();
		Connection conn = DBClass.returnConnection();
		PreparedStatement preStatement = conn.prepareStatement("select * from groups");
		ResultSet rs = preStatement.executeQuery();
		JSONArray  jsonArray =ToJSON.toJSONArray(rs);
	    rs.close();
	    preStatement.close();
	    conn.close();
	    //jsonArrayString = jsonArray.toString();
	   return jsonArray; 
	}
	

    @GET
    @Path("getGroupsOfAUser/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONArray getUsersGroups(@PathParam("email") String email) throws Exception
{

            Connection conn = DBClass.returnConnection();
    PreparedStatement preStatement = conn
                    .prepareStatement("SELECT groups_name FROM users_groups WHERE users_email = ?");
    preStatement.setString(1, email);
    ResultSet rs = preStatement.executeQuery();
    JSONArray  jsonArray =ToJSON.toJSONArray(rs);
    rs.close();
    preStatement.close();
    conn.close();
    return jsonArray; 
    }
	
	@GET
	@Path("getGroupInfo/{groupCookie}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getGroupInfo(@PathParam("groupCookie") String groupCookie) throws Exception
    {
		//JSONObject jsonObject = new JSONObject();
		String group = groupCookie;
		Connection conn = DBClass.returnConnection();
		PreparedStatement preStatement = conn.prepareStatement("select * from groups where name = ?");
		preStatement.setString(1, group);
		ResultSet rs = preStatement.executeQuery();
		JSONArray  jsonArray =ToJSON.toJSONArray(rs);
	    rs.close();
	    preStatement.close();
	    conn.close();
	    //jsonArrayString = jsonArray.toString();
	   return jsonArray; 
	}
	
	@GET
	@Path("getComments/{groupCookie}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getComments(@PathParam("groupCookie") String groupCookie) throws Exception
    {
		//JSONObject jsonObject = new JSONObject();
		String group = groupCookie;
		Connection conn = DBClass.returnConnection();
		PreparedStatement preStatement = conn.prepareStatement("select * from comments where groups_name = ? ORDER BY presentTime DESC");
		preStatement.setString(1, group);
		ResultSet rs = preStatement.executeQuery();
		JSONArray  jsonArray =ToJSON.toJSONArray(rs);
	    rs.close();
	    preStatement.close();
	    conn.close();
	    //jsonArrayString = jsonArray.toString();
	   return jsonArray; 
	}
	
	@GET
	@Path("judgeGroupJoined/{groupCookie}/{user_email}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray judgeGroupJoined(@PathParam("groupCookie") String groupCookie,@PathParam("user_email") String user_email) throws Exception
    {
		//JSONObject jsonObject = new JSONObject();
		String group = groupCookie;
		String email = user_email;
		Connection conn = DBClass.returnConnection();
		PreparedStatement preStatement = conn.prepareStatement("select * from users_groups where users_email =? and groups_name = ?");
		preStatement.setString(1, user_email);
		preStatement.setString(2, groupCookie);
		ResultSet rs = preStatement.executeQuery();
		JSONArray  jsonArray =ToJSON.toJSONArray(rs);
	    rs.close();
	    preStatement.close();
	    conn.close();
	    //jsonArrayString = jsonArray.toString();
	   return jsonArray; 
	}
	
	@GET
	@Path("judgeAdmin/{groupCookie}/{user_email}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray judgeAdmin(@PathParam("groupCookie") String groupCookie,@PathParam("user_email") String user_email) throws Exception
    {
		//JSONObject jsonObject = new JSONObject();
		String group = groupCookie;
		String email = user_email;
		Connection conn = DBClass.returnConnection();
		PreparedStatement preStatement = conn.prepareStatement("select * from groups where admin_email =? and name = ?");
		preStatement.setString(1, user_email);
		preStatement.setString(2, groupCookie);
		ResultSet rs = preStatement.executeQuery();
		JSONArray  jsonArray =ToJSON.toJSONArray(rs);
	    rs.close();
	    preStatement.close();
	    conn.close();
	    //jsonArrayString = jsonArray.toString();
	   return jsonArray; 
	}
	
	@POST
	@Path("leaveGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response leaveGroup(String incomingData) throws Exception
    {
        JSONObject jsonObject = new JSONObject(incomingData);
        String group = jsonObject.getString("groupName");
        String email = jsonObject.getString("userEmail");
		//JSONObject jsonObject = new JSONObject();
		Connection conn = DBClass.returnConnection();
		PreparedStatement preStatement = conn.prepareStatement("delete from users_groups where users_email =? and groups_name = ?");
		preStatement.setString(1, email);
		preStatement.setString(2, group);
		int i = preStatement.executeUpdate();
	    preStatement.close();
	    if(i>=0){
		PreparedStatement preStatement1 = conn.prepareStatement("UPDATE groups set numberOfMember = numberOfMember - 1 WHERE name = ?");
		preStatement1.setString(1, group);
		int j = preStatement1.executeUpdate();
	    preStatement1.close();
	    conn.close();
	    if(j>=0)
	    {
	    	conn.close();
	    	return Response.status(200).entity("update ok").build();
	    }
	    else
	    {
	    	conn.close();
	    	return Response.status(400).entity("update error").build();
	    }
	    }
	    else
	    {
	    	conn.close();
	    	return Response.status(400).entity("delete error").build();
	    }
	}
	
	@POST
	@Path("joinGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response joinGroup(String incomingData) throws Exception
    {
        JSONObject jsonObject = new JSONObject(incomingData);
        String group = jsonObject.getString("groupName");
        String email = jsonObject.getString("userEmail");
		//JSONObject jsonObject = new JSONObject();
		Connection conn = DBClass.returnConnection();
		PreparedStatement preStatement = conn.prepareStatement("insert users_groups (users_email,groups_name) values(?,?)");
		preStatement.setString(1, email);
		preStatement.setString(2, group);
		int i = preStatement.executeUpdate();
	    preStatement.close();
	    if(i>=0){
		PreparedStatement preStatement1 = conn.prepareStatement("UPDATE groups set numberOfMember = numberOfMember + 1 WHERE name = ?");
		preStatement1.setString(1, group);
		int j = preStatement1.executeUpdate();
	    preStatement1.close();
	    conn.close();
	    if(j>=0)
	    {
	    	conn.close();
	    	return Response.status(200).entity("update ok").build();
	    }
	    else
	    {
	    	conn.close();
	    	return Response.status(400).entity("update error").build();
	    }
	    }
	    else
	    {
	    	conn.close();
	    	return Response.status(400).entity("insert error").build();
	    }
	}
	
	
    
    @GET
    @Path("getMembers/{group}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONArray getMembers(@PathParam("group") String group) throws Exception
{

            Connection conn = DBClass.returnConnection();
    PreparedStatement preStatement = conn
                    .prepareStatement("SELECT users_email FROM users_groups WHERE groups_name = ?");
    preStatement.setString(1, group);
    ResultSet rs = preStatement.executeQuery();
    JSONArray  jsonArray =ToJSON.toJSONArray(rs);
    rs.close();
    preStatement.close();
    conn.close();
    return jsonArray; 
    }

    @POST
    @Path("editUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUser(String incomingData) throws Exception {
            JSONObject jsonObject = new JSONObject(incomingData);
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");
            String email = jsonObject.getString("email");
            String biography = jsonObject.getString("biography");
            Connection conn = DBClass.returnConnection();
            PreparedStatement preStatement = conn
                            .prepareStatement("update users set firstname = ?, lastname = ?, biography = ? where email = ?");
            preStatement.setString(1, firstname);
            preStatement.setString(2, lastname);
            preStatement.setString(3, biography);
            preStatement.setString(4, email);
            int i = preStatement.executeUpdate();
            System.out.println("i = " + i);
            preStatement.close();
            conn.close();
            if (i >= 0) {
                    System.out.println("on renvoie 200");
                    return Response.status(200).entity("ok").build();
            } else {
                    System.out.println("on renvoie 400");
                    return Response.status(400).entity(null).build();
            }
    }
    
    
    @POST
    @Path("editDescription")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editDescription(String incomingData) throws Exception {
            JSONObject jsonObject = new JSONObject(incomingData);
            String group = jsonObject.getString("group");
            String description = jsonObject.getString("description");
            Connection conn = DBClass.returnConnection();
            PreparedStatement preStatement = conn.prepareStatement("update groups set description = ? where name = ?");
            preStatement.setString(1, description);
            preStatement.setString(2, group);
            int i = preStatement.executeUpdate();
            preStatement.close();
            conn.close();
            if (i >= 0) {
                    
                    return Response.status(200).entity("ok").build();
            } else {
                   
                    return Response.status(400).entity(null).build();
            }
    }
    
    @POST
    @Path("addComment")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(String incomingData) throws Exception {
            JSONObject jsonObject = new JSONObject(incomingData);
            String username = jsonObject.getString("username");
            String group = jsonObject.getString("group");
            String comment = jsonObject.getString("comment");
            Connection conn = DBClass.returnConnection();
            PreparedStatement preStatement = conn.prepareStatement("insert into comments(comment,users_email,groups_name) VALUES (?,?,?)");
            preStatement.setString(1, comment);
            preStatement.setString(2, username);
            preStatement.setString(3, group);
            int i = preStatement.executeUpdate();
            preStatement.close();
            conn.close();
            if (i >= 0) {
                    
                    return Response.status(200).entity("ok").build();
            } else {
                   
                    return Response.status(400).entity(null).build();
            }
    }
	
	



    @POST
    @Path("deleteUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(String email) throws SQLException {
            Connection conn = DBClass.returnConnection();
            PreparedStatement preStatement0 = conn
                            .prepareStatement("SELECT groups_name FROM users_groups WHERE users_email = ?");
            preStatement0.setString(1, email);
            ResultSet rs = preStatement0.executeQuery();

            ArrayList<String> listOfName = new ArrayList<String>();
            int k = 0;
            while (rs.next()) {
                    listOfName.add(rs.getString(1));
                    k++;
            }
            preStatement0.close();
            PreparedStatement preStatement = conn
                            .prepareStatement("UPDATE groups set numberOfMember = numberOfMember - 1 WHERE name = ?");
            while (k > 0) {
                    preStatement.setString(1, listOfName.get(k - 1));
                    int i = preStatement.executeUpdate();
                    if (i < 0)
                            return Response.status(400).entity(null).build();
                    k--;
            }
            preStatement.close();
            PreparedStatement pStatement = conn.prepareStatement("delete from users where email = ?");
            pStatement.setString(1, email);
            int j = pStatement.executeUpdate();
            pStatement.close();
            conn.close();
            if (j >= 0)
                    return Response.status(200).entity("ok").build();
            else
                    return Response.status(400).entity(null).build();

    }
    
    @POST
    @Path("deleteGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteGroup(String group) throws SQLException {
            Connection conn = DBClass.returnConnection();
            PreparedStatement pStatement = conn.prepareStatement("delete from groups where name = ?");
            pStatement.setString(1, group);
            int j = pStatement.executeUpdate();
            pStatement.close();
            conn.close();
            if (j >= 0)
                    return Response.status(200).entity("ok").build();
            else
                    return Response.status(400).entity(null).build();

    }
    

	
    @POST
    @Path("createGroup")
    // @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGroup(String incomingData) throws SQLException, JSONException {
        JSONObject jsonObject = new JSONObject(incomingData);
        String name = jsonObject.getString("name");
        String description = jsonObject.getString("description");
        String admin = jsonObject.getString("admin");

        Connection conn = DBClass.returnConnection();


            PreparedStatement preStatement = conn.prepareStatement("insert into groups(name,description,admin_email,numberOfMember) values(?,?,?,?)");
            preStatement.setString(1, name);
            preStatement.setString(2, description);
            preStatement.setString(3, admin);
            preStatement.setInt(4, 1);
            int i = preStatement.executeUpdate();
            preStatement.close();
           if(i>=0){
        	   PreparedStatement preStatement4 = conn.prepareStatement("insert into users_groups(users_email,groups_name) values(?,?)");
        	   preStatement4.setString(1, admin);
        	   preStatement4.setString(2, name);
        	   int j = preStatement4.executeUpdate();
        	   if (j >= 0) {
        		   	conn.close();
        		   	return Response.status(200).entity("users_groups ok").build();
        	   } else {
        		   	conn.close();
        		   	return Response.status(400).entity("users_groups error").build();
        	   }
           }
           else 
           {
                conn.close();
                return Response.status(400).entity("groups error").build();
           }


    }

	
	

	
	@GET
	@Path("getUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsers() throws Exception
    {

		Connection conn = DBClass.returnConnection();
		PreparedStatement preStatement = conn.prepareStatement("select * from users");
		ResultSet rs = preStatement.executeQuery();
		JSONArray  jsonArray =ToJSON.toJSONArray(rs);
	    rs.close();
	    preStatement.close();
	    conn.close();
		return jsonArray.toString(); 
	}
	
    @GET
    @Path("getUser/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONArray getUsers(@PathParam("email") String email) throws Exception
    {
        System.out.println("email de jingjing : " + email);
        
        Connection conn = DBClass.returnConnection();
        PreparedStatement preStatement = conn.prepareStatement("select firstname, lastname, biography from users where email = ?");
        preStatement.setString(1, email);
        ResultSet rs = preStatement.executeQuery();
        JSONArray  jsonArray =ToJSON.toJSONArray(rs);
        System.out.println(jsonArray);
        rs.close();
        preStatement.close();
        conn.close();
       return jsonArray;
    }
	

	
	@GET
	@Path("getUsersGroups")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUsersGroups() throws Exception
    {

		Connection conn = DBClass.returnConnection();
		PreparedStatement preStatement = conn.prepareStatement("select * from users_groups");
		ResultSet rs = preStatement.executeQuery();
		JSONArray  jsonArray =ToJSON.toJSONArray(rs);
	    rs.close();
	    preStatement.close();
	    conn.close();
		return jsonArray.toString(); 
	}
	
	
	

}
