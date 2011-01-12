<%inherit file="/sbig.mako" />
<%namespace name="helper" file="/helper.mako" />

${len(c.results)}
<br>

% for result in c.results:
  ${helper.printResult(result)}
% endfor
