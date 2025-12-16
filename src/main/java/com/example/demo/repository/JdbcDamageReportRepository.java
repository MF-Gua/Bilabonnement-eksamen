package com.example.demo.repository;

import com.example.demo.model.DamageReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcDamageReportRepository implements DamageReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcDamageReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Mapper en række fra tabellen DamageReport til et Java-objekt
    private DamageReport mapRow(ResultSet rs, int rowNum) throws SQLException {
        DamageReport d = new DamageReport();
        d.setDamageId(rs.getLong("damage_id"));
        d.setDamageDate(rs.getDate("damage_date").toLocalDate());
        d.setDescription(rs.getString("description"));
        d.setRepairCost(rs.getDouble("repair_cost"));
        // Bemærk: Vi bruger registration_no (nummerplade) som reference til bilen.
        // Det matcher schema.sql, hvor DamageReport.registration_no er foreign key til Vehicle.registration_no.
        d.setRegistrationNo(rs.getString("registration_no"));
        d.setEmployeeId(rs.getLong("employee_id"));
        // lease_id er en foreign key til LeaseContract(lease_id).
        // Hvis lease_id ikke findes, vil INSERT fejle med foreign key constraint.
        d.setLeaseId(rs.getLong("lease_id"));
        d.setKmSlut(rs.getInt("km_slut"));
        return d;
    }

    @Override
    public void save(DamageReport report) {
        if (report.getDamageId() == null) {
            // INSERT: opret en ny skadesrapport
            // VIGTIGT: registration_no, employee_id og lease_id skal referere til eksisterende rækker pga. foreign keys.
            String sql = """
                INSERT INTO DamageReport
                (damage_date, description, repair_cost, registration_no, employee_id, lease_id, km_slut)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
            jdbcTemplate.update(sql,
                    report.getDamageDate(),
                    report.getDescription(),
                    report.getRepairCost(),
                    report.getRegistrationNo(),
                    report.getEmployeeId(),
                    report.getLeaseId(),
                    report.getKmSlut()
            );
        } else {
            // UPDATE: opdater en eksisterende skadesrapport
            String sql = """
                UPDATE DamageReport
                SET damage_date = ?, description = ?, repair_cost = ?, registration_no = ?,
                    employee_id = ?, lease_id = ?, km_slut = ?
                WHERE damage_id = ?
            """;
            jdbcTemplate.update(sql,
                    report.getDamageDate(),
                    report.getDescription(),
                    report.getRepairCost(),
                    report.getRegistrationNo(),
                    report.getEmployeeId(),
                    report.getLeaseId(),
                    report.getKmSlut(),
                    report.getDamageId()
            );
        }
    }

    @Override
    public List<DamageReport> findAll() {
        String sql = "SELECT * FROM DamageReport";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public List<DamageReport> findByRegistrationNo(String registrationNo) {
        String sql = "SELECT * FROM DamageReport WHERE registration_no = ?";
        return jdbcTemplate.query(sql, this::mapRow, registrationNo);
    }

    @Override
    public List<DamageReport> findByLeaseId(Long leaseId) {
        String sql = "SELECT * FROM DamageReport WHERE lease_id = ?";
        return jdbcTemplate.query(sql, this::mapRow, leaseId);
    }
}