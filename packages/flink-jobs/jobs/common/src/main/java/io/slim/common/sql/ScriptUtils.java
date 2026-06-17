package io.slim.common.sql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;

/**
 * @see https://github.com/spring-projects/spring-framework/blob/main/spring-jdbc/src/main/java/org/springframework/jdbc/datasource/init/ScriptUtils.java
 */
public class ScriptUtils {

    /**
	 * Default statement separator within SQL scripts: {@code ";"}.
	 */
	public static final String DEFAULT_STATEMENT_SEPARATOR = ";";

	/**
	 * Fallback statement separator within SQL scripts: {@code "\n"}.
	 * <p>Used if neither a custom separator nor the
	 * {@link #DEFAULT_STATEMENT_SEPARATOR} is present in a given script.
	 */
	public static final String FALLBACK_STATEMENT_SEPARATOR = "\n";

	/**
	 * End of file (EOF) SQL statement separator: {@code "^^^ END OF SCRIPT ^^^"}.
	 * <p>This value may be supplied as the {@code separator} to {@link
	 * #executeSqlScript(Connection, EncodedResource, boolean, boolean, String, String, String, String)}
	 * to denote that an SQL script contains a single statement (potentially
	 * spanning multiple lines) with no explicit statement separator. Note that
	 * such a script should not actually contain this value; it is merely a
	 * <em>virtual</em> statement separator.
	 */
	public static final String EOF_STATEMENT_SEPARATOR = "^^^ END OF SCRIPT ^^^";

	/**
	 * Default prefix for single-line comments within SQL scripts: {@code "--"}.
	 */
	public static final String DEFAULT_COMMENT_PREFIX = "--";

	/**
	 * Default prefixes for single-line comments within SQL scripts: {@code ["--"]}.
	 * @since 5.2
	 */
	public static final String[] DEFAULT_COMMENT_PREFIXES = {DEFAULT_COMMENT_PREFIX};

	/**
	 * Default start delimiter for block comments within SQL scripts: {@code "/*"}.
	 */
	public static final String DEFAULT_BLOCK_COMMENT_START_DELIMITER = "/*";

	/**
	 * Default end delimiter for block comments within SQL scripts: <code>"*&#47;"</code>.
	 */
	public static final String DEFAULT_BLOCK_COMMENT_END_DELIMITER = "*/";


	// private static final Log logger = LogFactory.getLog(ScriptUtils.class);

    static List<String> splitSqlScript(String script) {
        var statements = new ArrayList<String>();
        splitSqlScript(
            script,
            DEFAULT_STATEMENT_SEPARATOR, DEFAULT_COMMENT_PREFIXES,
            DEFAULT_BLOCK_COMMENT_START_DELIMITER, DEFAULT_BLOCK_COMMENT_END_DELIMITER,
            statements);
        return statements;
    }
    
    /**
	 * Split an SQL script into separate statements delimited by the provided
	 * separator string. Each individual statement will be added to the provided
	 * {@code List}.
	 * <p>Within the script, the provided {@code commentPrefixes} will be honored:
	 * any text beginning with one of the comment prefixes and extending to the
	 * end of the line will be omitted from the output. Similarly, the provided
	 * {@code blockCommentStartDelimiter} and {@code blockCommentEndDelimiter}
	 * delimiters will be honored: any text enclosed in a block comment will be
	 * omitted from the output. In addition, multiple adjacent whitespace characters
	 * will be collapsed into a single space.
	 * @param resource the resource from which the script was read
	 * @param script the SQL script
	 * @param separator text separating each statement
	 * (typically a ';' or newline character)
	 * @param commentPrefixes the prefixes that identify SQL line comments
	 * (typically "--")
	 * @param blockCommentStartDelimiter the <em>start</em> block comment delimiter;
	 * never {@code null} or empty
	 * @param blockCommentEndDelimiter the <em>end</em> block comment delimiter;
	 * never {@code null} or empty
	 * @param statements the list that will contain the individual statements
	 * @throws ScriptException if an error occurred while splitting the SQL script
	 * @since 5.2
	 */
	static void splitSqlScript(
            // @Nullable EncodedResource resource,
            String script,
			String separator, String[] commentPrefixes, String blockCommentStartDelimiter,
			String blockCommentEndDelimiter, List<String> statements) {

		// Assert.hasText(script, "'script' must not be null or empty");
		// Assert.notNull(separator, "'separator' must not be null");
		// Assert.notEmpty(commentPrefixes, "'commentPrefixes' must not be null or empty");
		// for (String commentPrefix : commentPrefixes) {
		// 	Assert.hasText(commentPrefix, "'commentPrefixes' must not contain null or empty elements");
		// }
		// Assert.hasText(blockCommentStartDelimiter, "'blockCommentStartDelimiter' must not be null or empty");
		// Assert.hasText(blockCommentEndDelimiter, "'blockCommentEndDelimiter' must not be null or empty");

		StringBuilder sb = new StringBuilder();
		boolean inSingleQuote = false;
		boolean inDoubleQuote = false;
		boolean inEscape = false;

		for (int i = 0; i < script.length(); i++) {
			char c = script.charAt(i);
			if (inEscape) {
				inEscape = false;
				sb.append(c);
				continue;
			}
			// MySQL style escapes
			if (c == '\\') {
				inEscape = true;
				sb.append(c);
				continue;
			}
			if (!inDoubleQuote && (c == '\'')) {
				inSingleQuote = !inSingleQuote;
			}
			else if (!inSingleQuote && (c == '"')) {
				inDoubleQuote = !inDoubleQuote;
			}
			if (!inSingleQuote && !inDoubleQuote) {
				if (script.startsWith(separator, i)) {
					// We've reached the end of the current statement
					if (sb.length() > 0) {
						statements.add(sb.toString());
						sb = new StringBuilder();
					}
					i += separator.length() - 1;
					continue;
				}
				else if (startsWithAny(script, commentPrefixes, i)) {
					// Skip over any content from the start of the comment to the EOL
					int indexOfNextNewline = script.indexOf('\n', i);
					if (indexOfNextNewline > i) {
						i = indexOfNextNewline;
						continue;
					}
					else {
						// If there's no EOL, we must be at the end of the script, so stop here.
						break;
					}
				}
				else if (script.startsWith(blockCommentStartDelimiter, i)) {
					// Skip over any block comments
					int indexOfCommentEnd = script.indexOf(blockCommentEndDelimiter, i);
					if (indexOfCommentEnd > i) {
						i = indexOfCommentEnd + blockCommentEndDelimiter.length() - 1;
						continue;
					}
					else {
						throw new IllegalArgumentException(
                            "Missing block comment end delimiter: " + blockCommentEndDelimiter);
					}
				}
				else if (c == ' ' || c == '\r' || c == '\n' || c == '\t') {
					// Avoid multiple adjacent whitespace characters
					if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
						c = ' ';
					}
					else {
						continue;
					}
				}
			}
			sb.append(c);
		}

		if (StringUtils.isNotBlank(sb)) {
			statements.add(sb.toString());
		}
	}

    private static boolean startsWithAny(String script, String[] prefixes, int offset) {
		for (String prefix : prefixes) {
			if (script.startsWith(prefix, offset)) {
				return true;
			}
		}
		return false;
	}

}
