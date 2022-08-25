import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import CircularProgress from '@mui/material/CircularProgress';

export default function BasicTable() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  async function fetchData(checkForChanges) {
    // You can await here
    setLoading(true);
    const result = await axios(
      //'https://tc-func-app1.azurewebsites.net/api/listTitusAuditLogs',
      '/api/listTitusAuditLogs2?checkForChanges=' + checkForChanges
    );
    setData(result.data);
    setLoading(false);
  }

  useEffect(() => {
    fetchData(false);
  }, []);

  return (
    <>
      <IconButton
        color="primary"
        aria-label="upload picture"
        component="span"
        onClick={() => {
          fetchData(true);
        }}
      >
        <RefreshIcon />
      </IconButton>
      {loading ? (
        <CircularProgress size={14} />
      ) : (
        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 }} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell>Document Name</TableCell>
                <TableCell align="right">User Name</TableCell>
                <TableCell align="right">Email Id</TableCell>
                <TableCell align="right">Access Type</TableCell>
                <TableCell align="right">ECI</TableCell>
                <TableCell align="right">ECICoC</TableCell>
                <TableCell align="right">ECI Juris</TableCell>
                <TableCell align="right">ECI Class</TableCell>
                <TableCell align="right">Export</TableCell>
                <TableCell align="right">Ex Auth</TableCell>
                <TableCell align="right">Contains CUI</TableCell>
                <TableCell align="right">CUI</TableCell>
                <TableCell align="right">Dissemination</TableCell>
                <TableCell align="right">Proprietary</TableCell>
                <TableCell align="right">Proprietary Type</TableCell>
                <TableCell align="right">Proprietary Statement</TableCell>
                <TableCell align="right">Site URL</TableCell>
                <TableCell align="right">Logged Time</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {data.map((row) => (
                <TableRow
                  key={row.documentName}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                  <TableCell component="th" scope="row">
                    {row.documentName}
                  </TableCell>
                  <TableCell align="right">{row.userName}</TableCell>
                  <TableCell align="right">{row.userId}</TableCell>
                  <TableCell align="right">{row.accessType}</TableCell>
                  <TableCell align="right">{row.eci}</TableCell>
                  <TableCell align="right">{row.eciCoC}</TableCell>
                  <TableCell align="right">{row.eciJuris}</TableCell>
                  <TableCell align="right">{row.eciClass}</TableCell>
                  <TableCell align="right">{row.export}</TableCell>
                  <TableCell align="right">{row.exAuth}</TableCell>
                  <TableCell align="right">{row.containsCUI}</TableCell>
                  <TableCell align="right">{row.cui}</TableCell>
                  <TableCell align="right">{row.dissemination}</TableCell>
                  <TableCell align="right">{row.proprietary}</TableCell>
                  <TableCell align="right">{row.proprietaryType}</TableCell>
                  <TableCell align="right">
                    {row.proprietaryStatement}
                  </TableCell>
                  <TableCell align="right">{row.siteFullPath}</TableCell>
                  <TableCell align="right">{row.loggedTime}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </>
  );
}
