package fr.harmonia.tsclientquery.query;

/*
 * Usage: use [schandlerid={scHandlerID}] [{scHandlerID}]
 * 
 * Selects the server connection handler scHandlerID or, if no parameter is
 * given, the currently active server connection handler is selected.
 * 
 * Examples: use schandlerid=1 selected schandlerid=1 error id=0 msg=ok
 * 
 * use 1 selected schandlerid=1 error id=0 msg=ok
 * 
 * 
 * error id=0 msg=ok
 * 
 */
public class UseQuery extends NoAnswerQuery {

	public UseQuery() {
		super("use");
	}

	public UseQuery(int schandlerid) {
		this();
		addArgument("schandlerid", schandlerid);
	}

}
