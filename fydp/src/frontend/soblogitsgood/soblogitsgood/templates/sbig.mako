## This template outlines the main page layout

<%inherit file="/sbig_base.mako" />
<br/>
<table>
  <tr>
    % if hasattr(c, "query"):
      <td class="sidebar">
        <ul class="views">
        <li>
          <span class="icon" style="background-image:
              url('images/list.png');"></span>
        % if c.service == "search":
          <span class="modePicked">List</span>
        % else:
          <a href="search?query=${c.query.replace(" ", "+")}"> List </a>
        % endif
        </li>

        <li>
          <span class="icon" style="background-image:
              url('images/polarize.png');"></span>
        % if c.service == "polarize":
          <span class="modePicked">Polarize</span>
        % else:
          <a href="polarize?query=${c.query.replace(" ", "+")}">Polarize</a>
        % endif
        </li>

        <li>
          <span class="icon" style="background-image:
              url('images/custom.png');"></span>
        % if c.service == "custom":
          <span class="modePicked">Custom</span>
        % else:
          <a href="custom?query=${c.query.replace(" ", "+")}">Custom</a>
        % endif
        </li>

        <li>
          <span class="icon" style="background-image:
              url('images/analysis.png');"></span>
        % if c.service == "analysis":
          <span class="modePicked">Analysis</span>
        % else:
          <a href="analysis?query=${c.query.replace(" ", "+")}">Analysis</a>
        % endif
        </li>
        </ul>
      </td>
    % endif
    <td class="content">
      ${next.body()}
    </td>
  </tr>
</table>
