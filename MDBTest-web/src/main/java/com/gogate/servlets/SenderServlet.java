/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gogate.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gogate
 */
public class SenderServlet extends HttpServlet {

    Context ic = null;
    ConnectionFactory cf = null;
    Connection connection = null;
    
    @Override
    public void init() throws ServletException{
       try {
            ic = new InitialContext();
            cf = (ConnectionFactory) ic.lookup("/ConnectionFactory");
 
        } catch (NamingException e) {
 
            e.printStackTrace();
        }        
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SenderServlet</title>");            
            out.println("</head>");
            out.println("<body>");

            String destinationName = "java:/queue/test";
            try {
                connection = cf.createConnection();
                Queue queue = (Queue) ic.lookup(destinationName);
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer publisher = session.createProducer(queue);
                connection.start();
                //TextMessage message = session.createTextMessage(request.getParameter("message"));
                TextMessage message = session.createTextMessage("yohoo");
                // publish the message to the defined Queue
                publisher.send(message);
                out.println("<h1>Message sent at " + request.getContextPath() + "</h1>");
            } catch(Exception e){
                out.println("<h1>Exception sending message at " + request.getContextPath() + "</h1>");
            }            
            
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
        

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
