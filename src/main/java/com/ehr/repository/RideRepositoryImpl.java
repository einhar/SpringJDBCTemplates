package com.ehr.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ehr.repository.util.RideRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ehr.model.Ride;

import javax.rmi.CORBA.PortableRemoteObjectDelegate;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Ride> getRides() {

		/*Ride ride = new Ride();
		ride.setName("Corner Canyon");
		ride.setDuration(120);
		List <Ride> rides = new ArrayList<>();
		rides.add(ride);*/

		List<Ride> rides = jdbcTemplate.query("SELECT * FROM ride", new RideRowMapper());

		return rides;
	}

	@Override
	public Ride createRide(Ride ride) {
		/*====== SIMPLE INSERT ======*/
		/*
		jdbcTemplate.update("INSERT INTO ride (name, duration) VALUES (?, ?)",
				ride.getName(), ride.getDuration());
		return ride;
		*/

		/*====== KEY HOLDER INSERT ======*/
		/*
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement("INSERT INTO ride (name, duration) VALUES (?, ?)",
								new String[] {"id"});
				ps.setString(1, ride.getName());
				ps.setInt(2, ride.getDuration());
				return ps;
			}
		}, keyHolder);
		Number id = keyHolder.getKey();
		return getRide(id.intValue());
		*/


		/*====== JDBC INSERT INSERT ======*/
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);

		List<String> columns = new ArrayList<>();
		columns.add("name");
		columns.add("duration");

		insert.setTableName("ride");
		insert.setColumnNames(columns);

		Map<String, Object> data = new HashMap<>();
		data.put("name", ride.getName());
		data.put("duration", ride.getDuration());

		insert.setGeneratedKeyName("id");
		Number key = insert.executeAndReturnKey(data);
		return getRide(key.intValue());

	}

	@Override
	public Ride getRide(Integer id) {
		Ride ride = jdbcTemplate.queryForObject("SELECT * FROM ride WHERE id = ?", new RideRowMapper(), id);
		return ride;
	}

	@Override
	public Ride updateRide(Ride ride) {
		jdbcTemplate.update("UPDATE ride SET name = ?, duration = ? WHERE id = ?",
				ride.getName(), ride.getDuration(), ride.getId());
		return ride;
	}

	@Override
	public void updateRides(List<Object[]> pairs) {
		jdbcTemplate.batchUpdate("UPDATE ride SET ride_date = ? WHERE id = ?",
				pairs);
	}

	@Override
	public void deleteRide(Integer id) {
		jdbcTemplate.update("DELETE FROM ride WHERE id = ?", id);
	}

	@Override
	public void deleteRideByParam(Integer id) {
		NamedParameterJdbcTemplate namedTemplate =
				new NamedParameterJdbcTemplate(jdbcTemplate);

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);

		namedTemplate.update("DELETE FROM ride WHERE id = :id", paramMap);
	}


}
