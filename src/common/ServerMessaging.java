/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * This class stores constants related to messages between the client and
 * server.
 *
 * Client -> Server message tags begin with IN_.
 *
 * Server -> Client message tags begin with OUT_.
 *
 * Error tags begin with ERROR_.
 *
 * @author Nathann Hohnbaum
 */
public class ServerMessaging
{
    // Message codes -----------------------------------------------------------
    // Incoming

    /**
     * Client is requesting to log into an already existing account
     */
    public static final String IN_LOGIN_REQUEST = "LOGIN";
    /**
     * Client is requesting to register a new account
     */
    public static final String IN_REGISTER_REQUEST = "REGISTER";
    /**
     * Client is requesting to play as a guest
     */
    public static final String IN_GUEST_REQUEST = "GUEST";
    /**
     * Client is requesting to log out
     */
    public static final String IN_LOGOUT_REQUEST = "LOGOUT";
    /**
     * Force a reply so that a reader that is waiting for a message receives one
     */
    public static final String IN_FLUSH = "FLUSH";
    /**
     * Client is sending invite
     */
    public static final String IN_SEND_INVITE = "INVITE";
    /**
     * Client cancels sent invite
     */
    public static final String IN_CANCEL_INVITE = "CANCEL";
    /**
     * Client rejects a received invite
     */
    public static final String IN_REJECT_INVITE = "REJECT";
    /**
     * Client accepts a received invite
     */
    public static final String IN_ACCEPT_INVITE = "ACCEPT";

    /**
     * Sender won a game against the specified user
     */
    public static final String IN_GAME_WIN = "WIN";
    
    /**
     * Sender drawed against the specified user
     */
    public static final String IN_GAME_DRAW = "DRAW";
    
    /**
     * Sent by the client to inform the server that the player list was received
     */
    public static final String IN_PLAYERS_RECEIVED = "RECEIVED_PLAYERS";
    
    // Outgoing
    /**
     * Client successfully logged in
     */
    public static final String OUT_LOGIN_SUCCESS = "LOGIN_SUCCESS";
    /**
     * Client was unable to be logged in
     */
    public static final String OUT_LOGIN_FAILURE = "LOGIN_FAILURE";
    /**
     * Client successfully registered a new account
     */
    public static final String OUT_REGISTER_SUCCESS = "LOGIN_SUCCESS";
    /**
     * Client successfully logged out
     */
    public static final String OUT_LOGOUT_SUCCESS = "LOGOUT";
    /**
     * Client was unable to register a new account
     */
    public static final String OUT_REGISTER_FAILURE = "REGISTER_FAILURE";
    /**
     * client will be disconnected
     */
    public static final String OUT_DISCONNECTING = "DISCONNECT";
    /**
     * An error occurred processing the client's request
     */
    public static final String OUT_ERROR = "ERROR";
    /**
     * The server is sending the player list
     */
    public static final String OUT_PLAYERS = "PLAYERS";
    /**
     * Provide a response so that a buffered reader waiting for a message
     * receives one
     */
    public static final String OUT_FLUSH = "FLUSH";
    /**
     * Sent to the receiving user of an invite that is closed without beginning
     * a game
     */
    public static final String OUT_CANCELED_INVITE = "INVITE_CANCELLED";
    /**
     * Sent to the receiving user of an invite when the invite is sent
     */
    public static final String OUT_RECEIVED_INVITE = "O_INVITE";
    /**
     * Sent to the sending user of an invite when the invite is accepted
     */
    public static final String OUT_ACCEPTED_INVITE = "ACCEPTED";
    /**
     * Sent to the sending user of an invite that is closed without beginning a game
     */
    public static final String OUT_REJECTED_INVITE = "REJECTED";
    /**
     * Sent when an error involving an invite operation occurs
     */
    public static final String OUT_INVITE_ERROR = "INVITE_ERROR";
    
    
    
    // Errors
    /**
     * Client sent an unknown request
     */
    public static final String ERROR_UNKNOWN_MESSAGE = "UNKNOWN_MESSAGE";
    /**
     * Client sent an improperly formatted request
     */
    public static final String ERROR_MESSAGE_FORMAT = "BAD_MESSAGE_FORMAT";
    /**
     * Error message for client needing to login or play as a guest in order to
     * access a particular feature
     */
    public static final String ERROR_LOGIN = "NEEDS_LOGIN";
    /**
     * Error message for client needing to login to a non-guest account in order
     * to access a particular feature
     */
    public static final String ERROR_REGISTER = "NEEDS_REGISTER";
    /**
     * Append to an error message if the cause of the error is unknown
     */
    public static final String ERROR_UNKNOWN = "UNKNOWN_ERROR";
    /**
     * Error message for when there is an illegal character in a username or
     * password
     */
    public static final String ERROR_ILLEGAL_CHAR = "ILLEGAL_CHARACTER";
    /**
     * Error message for when an incorrect username/password combo is given with
     * a login request
     */
    public static final String ERROR_BAD_LOGIN = "BAD_LOGIN";
    /**
     * Error message for when the client attempts to login twicce without
     * logging out between
     */
    public static final String ERROR_ALREADY_LOGGED_IN = "ALREADY_LOGGED_IN";
    /**
     * Error message for when a client attempts to login to an account already
     * logged into by another client
     */
    public static final String ERROR_DUPLICATE_LOGIN = "DUPLICATE_LOGIN";
    /**
     * Error message for when a register request is rejected because the
     * username is already taken
     */
    public static final String ERROR_USERNAME_TAKEN = "USERNAME_TAKEN";
    /**
     * Error message for when a register request is rejected because the
     * username is invalid for reasons other than being already taken
     */
    public static final String ERROR_BAD_USERNAME = "BAD_USERNAME";
    /**
     * An error occurred when sending an invite
     */
    public static final String ERROR_SENDING_INVITE = "SENT_INVITE_ERROR";
    /**
     * An error occurred when cancelling an invite
     */
    public static final String ERROR_CANCELLING_INVITE = "CANCEL_INVITE_ERROR";
    /**
     * An error occurred when accepting an invite
     */
    public static final String ERROR_ACCEPTING_INVITE = "RECEIVED_INVITE_ERROR";
    /**
     * An error occurred when rejecting an invite
     */
    public static final String ERROR_REJECTING_INVITE = "REJECTED_INVITE_ERROR";
    
    /**
     * Networking error occurred
     */
    public static final String ERROR_CONNECTION = "CONNECTION_ERROR";
    
    
    // Ports
    /**
     * The port on which the server runs
     */
    public static final int SERVER_PORT = 69;
    
    /**
     * The port to run game play connections on
     */
    public static final int GAMEPLAY_PORT = 8085;
}
