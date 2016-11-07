package persistency;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entities.QuizReport;
import util.DatabaseConnector;

public enum QuizReportMapper {
	INSTANCE;
	
	private QuizReportMapper() {
		
	}
	
	public void createQuizReport(QuizReport quizReport)
	{		
		String sql = "INSERT INTO QuizReports (quizId, teamId, seasonId, roundScores) VALUES (?,?,?,?)";
		try (PreparedStatement pstmt = DatabaseConnector.INSTANCE.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, quizReport.getQuiz().getId());
			pstmt.setInt(2, quizReport.getTeam().getId());
			pstmt.setInt(3, quizReport.getSeason().getId());
			pstmt.setString(4, arrayToCsv(quizReport.getRoundScores()));			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int updateQuizReport(QuizReport quizReport) {
		int rowsAffected = 0;
		String sql = "UPDATE People SET roundScores = ? WHERE quizId = ? AND teamId = ?";
		try (PreparedStatement pstmt = DatabaseConnector.INSTANCE.getConnection().prepareStatement(sql)) {
			pstmt.setString(1, arrayToCsv(quizReport.getRoundScores()));
			pstmt.setInt(2, quizReport.getQuiz().getId());
			pstmt.setInt(3, quizReport.getTeam().getId());					
			 // executeUpdate() should be called to change something in the database
			rowsAffected = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowsAffected;
	}
	
	
	private String arrayToCsv(ArrayList<Integer> input)
	{
		String result = "";
		for (int in : input)
		{
			result += in + ",";
		}
		
		return result.substring(0, result.length() - 1);
	}
	
	private ArrayList<Integer> csvToArray(String input)
	{
		ArrayList<Integer> out = new ArrayList<Integer>();
		for(String s : input.split("\\s*,\\s*"))
		{
			out.add(Integer.parseInt(s));
		}
		return out;
	}
}
