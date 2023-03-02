package com.uniquedeveloper.registration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UploadIntoData
 */
@WebServlet("/UploadIntoData")
@MultipartConfig
public class UploadIntoData extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 // Get the uploaded file
		  Part filePart = request.getPart("audioFile");
		  InputStream fileContent = filePart.getInputStream();
		  ByteArrayOutputStream output = new ByteArrayOutputStream();
		  byte[] buffer = new byte[1024];
		  int length;
		  while ((length = fileContent.read(buffer)) != -1) {
		    output.write(buffer, 0, length);
		  }
		  byte[] fileBytes = output.toByteArray();
		  String audio_id=request.getParameter("audio_id");
		  // Insert the file into the database
		  Connection conn = null;
		  PreparedStatement stmt = null;
		  try {
			Class.forName("com.mysql.jdbc.Driver");
		    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "root");
		    stmt = conn.prepareStatement("INSERT INTO audio_files (audio_id,filedata) VALUES (?,?)");
		    stmt.setString(1, audio_id);
		    stmt.setBytes(2, fileBytes);
		    stmt.executeUpdate();
		    PrintWriter t=response.getWriter();
		    t.print("done");
		  } catch (SQLException e) {
		    e.printStackTrace();
		  } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try {
		      if (stmt != null) stmt.close();
		      if (conn != null) conn.close();
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
		  }
		  
	}

}
