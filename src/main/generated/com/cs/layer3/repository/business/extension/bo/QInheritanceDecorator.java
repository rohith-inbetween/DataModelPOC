package com.cs.layer3.repository.business.extension.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QInheritanceDecorator is a Querydsl query type for InheritanceDecorator
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInheritanceDecorator extends EntityPathBase<InheritanceDecorator> {

    private static final long serialVersionUID = -1206494242L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInheritanceDecorator inheritanceDecorator = new QInheritanceDecorator("inheritanceDecorator");

    public final com.cs.layer3.repository.business.defalt.bo.QBusinessObject _super = new com.cs.layer3.repository.business.defalt.bo.QBusinessObject(this);

    public final com.cs.layer3.repository.business.defalt.bo.QEntity child;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.cs.layer3.repository.business.defalt.bo.QEntity parent;

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public QInheritanceDecorator(String variable) {
        this(InheritanceDecorator.class, forVariable(variable), INITS);
    }

    public QInheritanceDecorator(Path<? extends InheritanceDecorator> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QInheritanceDecorator(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QInheritanceDecorator(PathMetadata<?> metadata, PathInits inits) {
        this(InheritanceDecorator.class, metadata, inits);
    }

    public QInheritanceDecorator(Class<? extends InheritanceDecorator> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.child = inits.isInitialized("child") ? new com.cs.layer3.repository.business.defalt.bo.QEntity(forProperty("child")) : null;
        this.parent = inits.isInitialized("parent") ? new com.cs.layer3.repository.business.defalt.bo.QEntity(forProperty("parent")) : null;
    }

}

