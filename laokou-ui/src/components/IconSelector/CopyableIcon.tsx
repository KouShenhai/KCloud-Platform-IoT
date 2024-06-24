import * as React from 'react';
import { Tooltip } from 'antd';
import classNames from 'classnames';
import * as AntdIcons from '@ant-design/icons';
import type { ThemeType } from './index';
import styles from './style.less';

const allIcons: {
  [key: string]: any;
} = AntdIcons;

export interface CopyableIconProps {
  name: string;
  isNew: boolean;
  theme: ThemeType;
  justCopied: string | null;
  onSelect: (type: string, text: string) => any;
}

const CopyableIcon: React.FC<CopyableIconProps> = ({
  name,
  justCopied,
  onSelect,
  theme,
}) => {
  const className = classNames({
    copied: justCopied === name,
    [theme]: !!theme,
  });
  return (
    <li className={className}
      onClick={() => {
        if (onSelect) {
          onSelect(theme, name);
        }
      }}>
      <Tooltip title={name}>
        {React.createElement(allIcons[name], { className: styles.anticon })}
      </Tooltip>
      {/* <span className={styles.anticonClass}>
          <Badge dot={isNew}>{name}</Badge>
        </span> */}
    </li>
  );
};

export default CopyableIcon;
