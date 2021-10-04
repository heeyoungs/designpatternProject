package library.service;

import database.PostgresqlAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectService {
    public void showUserList() { // 손님 목록 출력
        System.out.println("사용자 목록을 보여줍니다.");
        final String query = "SELECT * FROM 손님";
        try (Connection connection = PostgresqlAccess.setConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery();) {
            while (rs.next()) {
                System.out.print("[이름] " + rs.getString(2) + "||");
                System.out.println("[전화번호] " + rs.getString(1));
            }
        } catch (SQLException sqex) {
            System.out.println("SQLException: " + sqex.getMessage());
            System.out.println("SQLState: " + sqex.getSQLState());
        }
    }

    public void showBookList() { // 도서 목록 출력
        System.out.println("도서목록을 보여줍니다.");
        final String query = "SELECT * FROM 도서목록 ORDER BY 책번호";
        try (Connection connection = PostgresqlAccess.setConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery();) {
            while (rs.next()) {
                System.out.println("[책 번호] " + rs.getString(1));
                System.out.print("[책 이름] " + rs.getString(2) + "||");
                System.out.print("[책 작가] " + rs.getString(3) + "||");
                System.out.print("[책 페이지]" + rs.getString(4) + "||");
                System.out.print("[책 출판년도]" + rs.getString(5) + "||");
                System.out.println("[책 대출 가능여부]" + rs.getString(6));
            }
        } catch (SQLException sqex) {
            System.out.println("SQLException: " + sqex.getMessage());
            System.out.println("SQLState: " + sqex.getSQLState());
        }
    }

    public void showBorrowBookList(String phoneNum) {
        final String query = "SELECT 도서목록.책번호, 도서목록.책이름 FROM 도서목록, 대여 WHERE 대여.전화번호= ? AND 대여.책번호 = 도서목록.책번호 ORDER BY 책번호";
        try (Connection connection = PostgresqlAccess.setConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            pstmt.setString(1, phoneNum);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.print("[책 번호] " + rs.getInt(1) + "||");
                System.out.println("[책 이름] " + rs.getString(2));
            }
        } catch (SQLException sqex) {
            sqex.printStackTrace();
        }
    }

    public String getOwnerPassword() { // 주인 로그인
        final String query = "SELECT 비밀번호 FROM 주인";
        try (Connection connection = PostgresqlAccess.setConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException sqex) {
            return null;
        }
    }

    public String getCustomerName(String phoneNum) {
        final String query = "SELECT 이름 FROM 손님 WHERE 전화번호=?";
        try (Connection connection = PostgresqlAccess.setConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            pstmt.setString(1, phoneNum);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (SQLException sqex) {
            return "-1";
        }
    }

    public boolean isBookExist(int bookNum) {
        final String query = "SELECT 책대출가능여부 FROM 도서목록 WHERE 책번호=?";
        try (Connection connection = PostgresqlAccess.setConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            pstmt.setInt(1, bookNum);
            ResultSet rs = pstmt.executeQuery();

            rs.next();
            if (rs.getBoolean(1)) {
                return true;
            }
        } catch (SQLException sqex) {
            return false;
        }
        return false;
    }

    public boolean isBorrow(int bookNum, String phoneNum) {
        final String query = "SELECT EXISTS (SELECT 책번호 FROM 대여 WHERE 책번호=? AND 전화번호=?)";
        try (Connection connection = PostgresqlAccess.setConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            pstmt.setInt(1, bookNum);
            pstmt.setString(2, phoneNum);
            ResultSet rs = pstmt.executeQuery();

            rs.next();
            if (rs.getBoolean(1)) {
                return true;
            }
        } catch (SQLException sqex) {
            return false;
        }
        return false;
    }

    public boolean isCustomerExist(String phoneNum) { // 손님 로그인
        final String query = "SELECT EXISTS (SELECT 전화번호 FROM 손님 WHERE 전화번호=?)";
        try (Connection connection = PostgresqlAccess.setConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
        ) {
            pstmt.setString(1, phoneNum);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if (rs.getBoolean(1)) {
                return true;
            }
        } catch (SQLException sqex) {
            return false;
        }
        return false;
    }

}