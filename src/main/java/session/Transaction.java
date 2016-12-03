package session;

import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Session;

public class Transaction {

	HashMap<Session,ArrayList> transaction = new HashMap<Session,ArrayList>();
}
